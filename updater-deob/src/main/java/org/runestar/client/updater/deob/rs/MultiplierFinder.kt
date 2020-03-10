package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.LONG_TYPE
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.objectweb.asm.tree.analysis.BasicValue
import org.objectweb.asm.tree.analysis.Interpreter
import org.objectweb.asm.tree.analysis.Value
import org.runestar.client.updater.common.invert
import org.runestar.client.updater.common.isInvertible
import org.runestar.client.updater.deob.Transformer
import java.nio.file.Path
import java.util.Collections
import java.util.TreeMap

object MultiplierFinder : Transformer.Tree() {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        val decoders = MultimapBuilder.hashKeys().arrayListValues().build<String, Number>()
        val dependentDecoders = MultimapBuilder.hashKeys().arrayListValues().build<String, Pair<String, Number>>()
        val dependentEncoders = MultimapBuilder.hashKeys().arrayListValues().build<String, Pair<String, Number>>()

        val analyzer = Analyzer(Inter(decoders, dependentDecoders, dependentEncoders))

        for (c in klasses) {
            for (m in c.methods) {
                findDupPutDecoders(m, decoders)
                analyzer.analyze(c.name, m)
            }
        }

        val decodersFinal = unfoldToDecoders(decoders, dependentDecoders, dependentEncoders)

        mapper.writeValue(dir.resolve( "mult.json").toFile(), decodersFinal)
        logger.info { "Multipliers found: ${decodersFinal.size}" }
    }

    private fun isMultiplier(n: Number): Boolean {
        return isInvertible(n) && invert(n) != n
    }
    
    private fun findDupPutDecoders(m: MethodNode, decoders: Multimap<String, Number>) {
        for (insn in m.instructions) {
            val ldc = insn as? LdcInsnNode ?: continue
            val cst = ldc.cst
            if (cst !is Int && cst !is Long) continue
            val put = ldc.previous as? FieldInsnNode ?: continue
            val dup = put.previous ?: continue
            val mul = ldc.next ?: continue
            if (mul.opcode != IMUL && mul.opcode != LMUL) continue

            if (
                    (put.opcode == PUTFIELD  && put.desc == INT_TYPE.descriptor  && dup.opcode == DUP_X1) ||
                    (put.opcode == PUTFIELD  && put.desc == LONG_TYPE.descriptor && dup.opcode == DUP2_X1) ||
                    (put.opcode == PUTSTATIC && put.desc == INT_TYPE.descriptor  && dup.opcode == DUP) ||
                    (put.opcode == PUTSTATIC && put.desc == LONG_TYPE.descriptor && dup.opcode == DUP2)
            ) {
                val fieldString = "${put.owner}.${put.name}"
                decoders.put(fieldString, cst as Number)
            }
        }
    }

    private fun unfoldToDecoders(
            decoders: Multimap<String, Number>,
            dependentDecoders: Multimap<String, Pair<String, Number>>,
            dependentEncoders: Multimap<String, Pair<String, Number>>
    ): Map<String, Number> {

        val decodersFinal = TreeMap<String, Number>()

        decoders.asMap().mapValuesTo(decodersFinal) { e ->
            checkNotNull(e.value.maxBy { n -> Collections.frequency(e.value, n) })
        }

        var startSize: Int
        do {
            startSize = decodersFinal.size

            dependentDecoders.entries().forEach { (f, p) ->
                if (f !in decodersFinal) {
                    val otherF = p.first
                    val value = p.second
                    val otherDecoder = decodersFinal[otherF] ?: return@forEach
                    val nUnfolded: Number = when (value) {
                        is Int -> value.toInt() * otherDecoder.toInt()
                        is Long -> value.toLong() * otherDecoder.toLong()
                        else -> error(value)
                    }
                    if (isMultiplier(nUnfolded)) {
                        decodersFinal[f] = nUnfolded
                    }
                }
            }

            dependentEncoders.entries().forEach { (f, p) ->
                if (f !in decodersFinal) {
                    val otherF = p.first
                    val value = p.second
                    val otherDecoder = decodersFinal[otherF] ?: return@forEach
                    val nUnfolded: Number = when (value) {
                        is Int -> value.toInt() * invert(otherDecoder.toInt())
                        is Long -> value.toLong() * invert(otherDecoder.toLong())
                        else -> error(value)
                    }
                    if (isMultiplier(nUnfolded)) {
                        decodersFinal[f] = invert(nUnfolded)
                    }
                }
            }

        } while (startSize != decodersFinal.size)

        return decodersFinal
    }

    private class Inter(
            val decoders: Multimap<String, Number>,
            val dependentDecoders: Multimap<String, Pair<String, Number>>,
            val dependentEncoders: Multimap<String, Pair<String, Number>>
    ) : Interpreter<Expr>(Opcodes.ASM6) {

        /*
            FIELD = LDC
                FIELD = enc * X
                enc == LDC / X
            FIELD = LDC * X
                FIELD = enc * X
                enc == LDC
            X = FIELD * LDC
                X = FIELD * dec
                dec == LDC
            FIELD = FIELD2 * LDC
                FIELD = FIELD2 * dec2 * enc
                enc == LDC / dec2
                enc == LDC * enc2
            FIELD2 = FIELD * LDC
                FIELD2 = FIELD * dec * enc2
                dec == LDC / enc2
                dec == LDC * dec2
            FIELD = FIELD + LDC
                FIELD = (FIELD * dec + X) * enc
                FIELD = FIELD + X * enc
                enc == LDC / X
            FIELD = FIELD + FIELD2 * LDC
                FIELD = (FIELD * dec + FIELD2 * dec2) * enc
                FIELD = FIELD + FIELD2 * dec2 * enc
                enc == LDC / dec2
                enc == LDC * enc2
         */

        private val basicInterpreter = BasicInterpreter()

        override fun binaryOperation(insn: AbstractInsnNode, value1: Expr, value2: Expr): Expr? {
            val bv = basicInterpreter.binaryOperation(insn, value1.basicValue, value2.basicValue)
            return when (insn.opcode) {
                IMUL, LMUL -> {
                    val ldc = value1 as? Expr.IntegerLdc ?: value2 as? Expr.IntegerLdc ?: return Expr.Var(bv)
                    val gf = value1 as? Expr.GetField ?: value2 as? Expr.GetField ?: return Expr.LdcMult(bv, ldc.n)
                    if (isMultiplier(ldc.n)) {
                        decoders.put("${gf.insn.owner}.${gf.insn.name}", ldc.n)
                    }
                    Expr.GetFieldMult(bv, gf.insn, ldc.n)
                }
                PUTFIELD -> {
                    putField(insn as FieldInsnNode, value2)
                    null
                }
                else -> bv?.let { Expr.Var(it) }
            }
        }

        override fun copyOperation(insn: AbstractInsnNode, value: Expr): Expr = value

        override fun merge(value1: Expr, value2: Expr): Expr {
            return if (value1.basicValue == value2.basicValue) {
                value1
            } else {
                Expr.Var(basicInterpreter.merge(value1.basicValue, value2.basicValue))
            }
        }

        override fun naryOperation(insn: AbstractInsnNode, values: MutableList<out Expr>): Expr? {
            val bv = basicInterpreter.naryOperation(insn, emptyList()) ?: return null
            return Expr.Var(bv)
        }

        override fun newOperation(insn: AbstractInsnNode): Expr {
            val bv = basicInterpreter.newOperation(insn)
            return when (insn.opcode) {
                LDC ->  {
                    insn as LdcInsnNode
                    when (insn.cst) {
                        is Int, is Long -> Expr.IntegerLdc(bv, insn.cst as Number)
                        else -> Expr.Var(bv)
                    }
                }
                GETSTATIC -> getField(bv, insn as FieldInsnNode)
                else -> Expr.Var(bv)
            }
        }

        private fun getField(bv: BasicValue, insn: FieldInsnNode): Expr {
            return when (insn.desc) {
                INT_TYPE.descriptor, LONG_TYPE.descriptor -> Expr.GetField(bv, insn)
                else -> Expr.Var(bv)
            }
        }

        private fun putField(insn: FieldInsnNode, value: Expr) {
            val destName = "${insn.owner}.${insn.name}"
            if (value is Expr.IntegerLdc) {
//                val nf = unfold(value.n)
//                if (nf != null && isMultiplier(nf)) {
//                    decoders.put(destName, invert(nf))
//                }
            } else if (value is Expr.LdcMult) {
                if (isMultiplier(value.n)) {
                    decoders.put(destName, invert(value.n))
                }
            } else if (value is Expr.GetFieldMult) {
                val srcName = "${value.insn.owner}.${value.insn.name}"
                decoders.remove(srcName, value.n)
                dependentDecoders.put(srcName, destName to value.n)
                dependentEncoders.put(destName, srcName to value.n)
            }
        }

        override fun newValue(type: Type?): Expr? {
            val sv = basicInterpreter.newValue(type) ?: return null
            return Expr.Var(sv)
        }

        override fun returnOperation(insn: AbstractInsnNode, value: Expr, expected: Expr) {}

        override fun ternaryOperation(insn: AbstractInsnNode, value1: Expr, value2: Expr, value3: Expr): Expr? = null

        override fun unaryOperation(insn: AbstractInsnNode, value: Expr): Expr? {
            val bv = basicInterpreter.unaryOperation(insn, value.basicValue)
            return when (insn.opcode) {
                GETFIELD -> getField(bv, insn as FieldInsnNode)
                PUTSTATIC -> {
                    putField(insn as FieldInsnNode, value)
                    null
                }
                else -> bv?.let { Expr.Var(it) }
            }
        }
    }

    private sealed class Expr : Value {

        override fun getSize(): Int = basicValue.size

        abstract val basicValue: BasicValue

        data class Var(override val basicValue: BasicValue) : Expr()

        data class IntegerLdc(override val basicValue: BasicValue, val n: Number) : Expr()

        data class GetField(override val basicValue: BasicValue, val insn: FieldInsnNode) : Expr()

        data class GetFieldMult(override val basicValue: BasicValue, val insn: FieldInsnNode, val n: Number) : Expr()

        data class LdcMult(override val basicValue: BasicValue, val n: Number) : Expr()
    }
}
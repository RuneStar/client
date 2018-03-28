package org.runestar.client.updater.deob.jagex.mult

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import org.apache.bcel.Const
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.objectweb.asm.tree.*
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.objectweb.asm.tree.analysis.BasicValue
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.deob.writeJar
import java.nio.file.Path
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.log
import kotlin.math.min

object MultiplierFinder : Transformer {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)

        val decoders = MultimapBuilder.hashKeys().arrayListValues().build<String, Const>()
        val encoders = MultimapBuilder.hashKeys().arrayListValues().build<String, Const>()

        val candidateFields = candidateFields(classNodes)

        classNodes.forEach { c ->
            c.methods.forEach { m ->
                analyzeMethod(c, m, decoders, encoders)
            }
        }
        val result = unfoldToDecoders(decoders, encoders, candidateFields)

        val multFile = destination.resolveSibling(destination.fileName.toString() + ".mult.json").toFile()
        mapper.writeValue(multFile, result)

        if (source != destination) {
            writeJar(classNodes, destination)
        }
    }

    private fun analyzeMethod(
            c: ClassNode,
            m: MethodNode,
            decoders: Multimap<String, Const>,
            encoders: Multimap<String, Const>
    ) {

        val a = Analyzer<BasicValue>(BasicInterpreter())
        a.analyze(c.name, m)
        val frames = a.frames
        val insns = m.instructions.toArray()

        val slots = TreeMap<Int, Slot>()

        out@
        for (i in 0 until insns.size - 1) {
            val insn = insns[i]
            val op = insn.opcode
            when (op) {
                LDC -> {
                    insn as LdcInsnNode
                    val cst = insn.cst
                    if (cst is Int || cst is Long) {
                        slots[frames[i + 1].stackSize - 1] = Slot.Operand.Ldc(cst as Number)
                        continue@out
                    }
                }
                GETFIELD, GETSTATIC -> {
                    insn as FieldInsnNode
                    val desc = insn.desc
                    if (desc == INT_TYPE.descriptor || desc == LONG_TYPE.descriptor) {
                        val fieldName = "${insn.owner}.${insn.name}"
                        slots[frames[i + 1].stackSize - 1] = Slot.Operand.Field(fieldName)
                        continue@out
                    }
                }
                IMUL, LMUL -> {
                    val stackSize = frames[i].stackSize
                    val x = slots.remove(stackSize - 2)
                    val y = slots.remove(stackSize - 1)
                    if (x is Slot.Operand && y is Slot.Operand && x.javaClass != y.javaClass) {
                        val opf = x as? Slot.Operand.Field ?: y as Slot.Operand.Field
                        val opl = x as? Slot.Operand.Ldc ?: y as Slot.Operand.Ldc
                        slots[stackSize - 2] = Slot.Mul.Field(opl.n, opf.f)
                        continue@out
                    }
                    if (x is Slot.Operand.Ldc || y is Slot.Operand.Ldc) {
                        val opl = x as? Slot.Operand.Ldc ?: y as Slot.Operand.Ldc
                        slots[stackSize - 2] = Slot.Mul.Other(opl.n)
                        continue@out
                    }
                }
                PUTFIELD, PUTSTATIC -> {
                    insn as FieldInsnNode
                    val desc = insn.desc
                    if (desc == INT_TYPE.descriptor || desc == LONG_TYPE.descriptor) {
                        val stackSize = frames[i].stackSize
                        val x = slots.remove(stackSize - 1)
                        if (x != null) {
                            val fieldName = "${insn.owner}.${insn.name}"
                            if (x is Slot.Mul.Field) {
                                encoders.put(fieldName, Const.FieldFolded(x.n, x.f))
//                                decoders.put(x.f, Const.FieldFolded(x.n, fieldName))
                                continue@out
                            } else if (x is Slot.Mul.Other) {
                                encoders.put(fieldName, Const.Direct(x.n))
                                continue@out
                            }
                        }
                    }
                }
            }
            val validStackSize = min(
                    frames[i].stackSize - insn.stackConsumed + 1,
                    frames[i + 1].stackSize - insn.stackProduced + 1
            )
            while (slots.isNotEmpty() && slots.lastKey() > validStackSize) {
                val x = slots.remove(slots.lastKey())
                if (x is Slot.Mul.Field) {
                    decoders.put(x.f, Const.Direct(x.n))
                }
            }
        }
    }

    private fun unfoldToDecoders(
            decoders: Multimap<String, Const>,
            encoders: Multimap<String, Const>,
            candidateFields: Set<String>
    ): Map<String, Number> {
        val result = TreeMap<String, Number>()

        // todo

        val directDecoders = MultimapBuilder.hashKeys().arrayListValues().build<String, Number>()
        val indirectDecoders = MultimapBuilder.hashKeys().arrayListValues().build<String, Number>()

        decoders.forEach { fieldName, cst ->
            if (cst is Const.Direct) {
                if (isInvertible(cst.n)) {
                    directDecoders.put(fieldName, cst.n)
                } else {
                    val unfolded = unfold1(cst.n)
                    if (unfolded != null) {
                        indirectDecoders.put(fieldName, unfolded)
                    }
                }
            }
        }
        encoders.forEach { fieldName, cst ->
            if (cst is Const.Direct) {
                if (isInvertible(cst.n)) {
                    directDecoders.put(fieldName, invert(cst.n))
                } else {
                    val unfolded = unfold1(cst.n)
                    if (unfolded != null) {
                        indirectDecoders.put(fieldName, invert(unfolded))
                    }
                }
            }
        }

        candidateFields.forEach { f ->
            val direct = directDecoders.get(f) ?: return@forEach
            //            val indirect = indirectDecoders.get(f)

            val dir = direct.maxBy { Collections.frequency(direct, it) }
            if (dir != null) {
                result[f] = dir
            }
        }

        candidateFields.forEach { f ->
            val dir = result[f]
            if (dir == null) {
                val enc = encoders[f]
                val cst = enc.maxBy { Collections.frequency(enc, it) }
                if (cst is Const.FieldFolded) {
                    val other = result[cst.f]
                    if (other != null) {
                        val n: Number = when (other) {
                            is Int -> cst.n.toInt() / other
                            is Long -> cst.n.toLong() / other
                            else -> error(other)
                        }
                        val unfold = unfold1(n)
                        if (unfold != null) {
                            result[f] = invert(unfold)
                        }
                    }
                }
            }
        }

        return result
    }

    private val AbstractInsnNode.stackProduced: Int get() {
        if (opcode < 0) return 0
        return org.apache.bcel.Const.getConsumeStack(opcode).coerceAtLeast(0)
    }

    private val AbstractInsnNode.stackConsumed: Int get() {
        if (opcode < 0) return 0
        return org.apache.bcel.Const.getProduceStack(opcode).coerceAtLeast(0)
    }

    private fun candidateFields(classNodes: Iterable<ClassNode>): Set<String> {
        val set = HashSet<String>()
        classNodes.forEach { c ->
            c.fields.forEach { f ->
                if (f.desc == INT_TYPE.descriptor || f.desc == LONG_TYPE.descriptor) {
                    set.add("${c.name}.${f.name}")
                }
            }
        }
        return set
    }

    interface Slot {

        interface Operand : Slot {

            class Ldc(val n: Number) : Operand

            class Field(val f: String) : Operand
        }

        interface Mul : Slot {

            class Field(val n: Number, val f: String) : Mul

            class Other(val n: Number) : Mul
        }
    }

    interface Const {

        class Direct(val n: Number) : Const

        class FieldFolded(val n: Number, val f: String) : Const
    }
}
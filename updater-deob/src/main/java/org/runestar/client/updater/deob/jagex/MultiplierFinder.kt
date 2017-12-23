package org.runestar.client.updater.deob.jagex

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.collect.ArrayListMultimap
import org.runestar.client.updater.deob.Deobfuscator
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.deob.writeJar
import org.apache.bcel.Const
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.objectweb.asm.tree.analysis.BasicValue
import java.math.BigInteger
import java.nio.file.Path
import java.util.*

object MultiplierFinder : Deobfuscator {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source)
        val decoders = TreeMap<String, Number>()
        val decodersRaw = ArrayListMultimap.create<String, Number>()
        val encodersRaw = ArrayListMultimap.create<String, Number>()
        classNodes.forEach { c ->
            c.methods.forEach { m ->
                val a = Analyzer<BasicValue>(BasicInterpreter())
                a.analyze(c.name, m)
                val frames = a.frames
                var ldcStackSlots = TreeMap<Int, Number>()
                var getFieldStackSlots = TreeMap<Int, String>()
                var mulStackSlots = TreeMap<Int, Number>()
                m.instructions.toArray().asList().dropLast(1).forEachIndexed { i, insn ->
                    when (insn.opcode) {
                        Opcodes.LDC -> {
                            insn as LdcInsnNode
                            val cst = insn.cst
                            if (cst is Int || cst is Long) {
                                ldcStackSlots[frames[i + 1].stackSize - 1] = cst as Number
                                return@forEachIndexed
                            }
                        }
                        Opcodes.GETFIELD, Opcodes.GETSTATIC -> {
                            insn as FieldInsnNode
                            val desc = insn.desc
                            if (desc == Type.INT_TYPE.descriptor || desc == Type.LONG_TYPE.descriptor) {
                                val fieldName = "${insn.owner}.${insn.name}"
                                getFieldStackSlots[frames[i + 1].stackSize - 1] = fieldName
                                return@forEachIndexed
                            }
                        }
                        Opcodes.IMUL, Opcodes.LMUL -> {
                            val currStackSize = frames[i].stackSize
                            if (ldcStackSlots.isNotEmpty() && currStackSize - ldcStackSlots.lastKey() <= 2) {
                                if (getFieldStackSlots.isNotEmpty() && currStackSize - getFieldStackSlots.lastKey() <= 2) {
                                    decodersRaw.put(getFieldStackSlots.lastEntry().value, ldcStackSlots.lastEntry().value)
                                } else {
                                    mulStackSlots[currStackSize - 2] = ldcStackSlots.remove(ldcStackSlots.lastKey())!!
                                    return@forEachIndexed
                                }
                            }
                        }
                        Opcodes.PUTFIELD, Opcodes.PUTSTATIC -> {
                            insn as FieldInsnNode
                            val desc = insn.desc
                            if (desc == Type.INT_TYPE.descriptor || desc == Type.LONG_TYPE.descriptor) {
                                val currStackSize = frames[i].stackSize
                                if (mulStackSlots.isNotEmpty() && currStackSize - 1 == mulStackSlots.lastKey()) {
                                    val fieldName = "${insn.owner}.${insn.name}"
                                    encodersRaw.put(fieldName, mulStackSlots.remove(mulStackSlots.lastKey()))
                                }
                            }
                        }
                    }
                    val validStackSize = Math.min(
                            frames[i].stackSize - insn.stackConsumed + 1,
                            frames[i + 1].stackSize - insn.stackProduced + 1)
                    ldcStackSlots = TreeMap(ldcStackSlots.headMap(validStackSize))
                    getFieldStackSlots = TreeMap(getFieldStackSlots.headMap(validStackSize))
                    mulStackSlots = TreeMap(mulStackSlots.headMap(validStackSize))
                }
            }
        }
        val possibilities = ArrayListMultimap.create<String, Number>()
        decodersRaw.forEach { k, v ->
            unfold1(v)?.let { possibilities.put(k, it) }
        }
        encodersRaw.forEach { k, v ->
            unfold1(v)?.let { possibilities.put(k, invert(it)) }
        }
        possibilities.asMap().forEach { k, vc ->
            decoders[k] = vc.maxBy { Collections.frequency(vc, it) } ?: error(vc)
        }
        val multFile = destination.resolveSibling(destination.fileName.toString() + ".mult.json").toFile()
        mapper.writeValue(multFile, decoders)
        val ints = decoders.values.count { it is Int }
        val longs = decoders.values.count { it is Long }
        logger.info { "Multipliers found: ints: $ints, longs: $longs" }
        writeJar(classNodes, destination)
    }

    private val INT_INV_MOD = BigInteger.ONE.shiftLeft(Integer.SIZE)
    private val LONG_INV_MOD = BigInteger.ONE.shiftLeft(java.lang.Long.SIZE)

    private fun invert(n: Number): Number {
        return when (n) {
            is Int -> BigInteger.valueOf(n.toLong()).modInverse(INT_INV_MOD).toInt()
            is Long -> BigInteger.valueOf(n).modInverse(LONG_INV_MOD).toLong()
            else -> error(n)
        }
    }

    private fun isInvertible(n: Number): Boolean {
        return when(n) {
            is Int -> BigInteger.valueOf(n.toLong()).gcd(INT_INV_MOD) == BigInteger.ONE
            is Long -> BigInteger.valueOf(n).gcd(LONG_INV_MOD) == BigInteger.ONE
            else -> error(n)
        }
    }

    private fun unfold1(n: Number): Number? {
        val nLong = n.toLong()
        for (i in 1 until Math.sqrt(Math.abs(nLong).toDouble()).toInt()) {
            if (nLong % i == 0L) {
                val unfolded = nLong / i
                when (n) {
                    is Int -> unfolded.toInt().let { if (isInvertible(it)) return it }
                    is Long -> if (isInvertible(unfolded)) return unfolded
                }
            }
        }
        return null
    }

    private val AbstractInsnNode.stackProduced: Int get() {
        if (opcode < 0) return 0
        return Const.getConsumeStack(opcode).coerceAtLeast(0)
    }

    private val AbstractInsnNode.stackConsumed: Int get() {
        if (opcode < 0) return 0
        return Const.getProduceStack(opcode).coerceAtLeast(0)
    }
}
package org.runestar.client.updater.deob.rs.mult

import com.google.common.collect.Multimap
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.objectweb.asm.tree.analysis.BasicValue
import org.objectweb.asm.tree.analysis.Interpreter
import org.objectweb.asm.tree.analysis.Value

class MultInterpreter(
        val decoders: Multimap<String, Number>,
        val dependentDecoders: Multimap<String, Pair<String, Number>>,
        val dependentEncoders: Multimap<String, Pair<String, Number>>
) : Interpreter<MultInterpreter.MultValue>(Opcodes.ASM6) {

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
        FIELD2 = FIELD * LDC
            FIELD2 = FIELD * dec * enc2
            dec == LDC / enc2

        todo:
        FIELD = FIELD + LDC
            FIELD = (FIELD * dec + X) * enc
            FIELD = FIELD + X * enc
            enc == LDC / X
     */

    private val basicInterpreter = BasicInterpreter()

    override fun binaryOperation(insn: AbstractInsnNode, value1: MultValue, value2: MultValue): MultValue? {
        val bv = basicInterpreter.binaryOperation(insn, value1.basicValue, value2.basicValue)
        return when (insn.opcode) {
            Opcodes.IMUL, Opcodes.LMUL -> {
                val ldc = value1 as? MultValue.IntegerLdc ?: value2 as? MultValue.IntegerLdc ?: return MultValue.Simple(bv)
                val gf = value1 as? MultValue.GetField ?: value2 as? MultValue.GetField ?: return MultValue.LdcMult(bv, ldc.n)
                if (isMultiplier(ldc.n)) {
                    decoders.put("${gf.insn.owner}.${gf.insn.name}", ldc.n)
                }
                MultValue.GetFieldMult(bv, gf.insn, ldc.n)
            }
            Opcodes.PUTFIELD -> {
                putField(insn as FieldInsnNode, value2)
                null
            }
            else -> bv?.let { MultValue.Simple(it) }
        }
    }

    override fun copyOperation(insn: AbstractInsnNode, value: MultValue): MultValue = value

    override fun merge(value1: MultValue, value2: MultValue): MultValue {
        return if (value1.basicValue == value2.basicValue) {
            value1
        } else {
            MultValue.Simple(basicInterpreter.merge(value1.basicValue, value2.basicValue))
        }
    }

    override fun naryOperation(insn: AbstractInsnNode, values: MutableList<out MultValue>): MultValue? {
        val bv = basicInterpreter.naryOperation(insn, emptyList()) ?: return null
        return  MultValue.Simple(bv)
    }

    override fun newOperation(insn: AbstractInsnNode): MultValue {
        val bv = basicInterpreter.newOperation(insn)
        return when (insn.opcode) {
            Opcodes.LDC ->  {
                insn as LdcInsnNode
                when (insn.cst) {
                    is Int, is Long -> MultValue.IntegerLdc(bv, insn.cst as Number)
                    else -> MultValue.Simple(bv)
                }
            }
            Opcodes.GETSTATIC -> getField(bv, insn as FieldInsnNode)
            else -> MultValue.Simple(bv)
        }
    }

    private fun getField(bv: BasicValue, insn: FieldInsnNode): MultValue {
        return when (insn.desc) {
            Type.INT_TYPE.descriptor, Type.LONG_TYPE.descriptor -> MultValue.GetField(bv, insn)
            else -> MultValue.Simple(bv)
        }
    }

    private fun putField(insn: FieldInsnNode, value: MultValue) {
        val destName = "${insn.owner}.${insn.name}"
        if (value is MultValue.IntegerLdc) {
            val nf = unfold(value.n)
            if (nf != null && isMultiplier(nf)) {
                decoders.put(destName, invert(nf))
            }
        } else if (value is MultValue.LdcMult) {
            if (isMultiplier(value.n)) {
                decoders.put(destName, invert(value.n))
            }
        } else if (value is MultValue.GetFieldMult) {
            val srcName = "${value.insn.owner}.${value.insn.name}"
            decoders.remove(srcName, value.n)
            dependentDecoders.put(srcName, destName to value.n)
            dependentEncoders.put(destName, srcName to value.n)
        }
    }

    override fun newValue(type: Type?): MultValue? {
        val sv = basicInterpreter.newValue(type) ?: return null
        return MultValue.Simple(sv)
    }

    override fun returnOperation(insn: AbstractInsnNode, value: MultValue, expected: MultValue) {}

    override fun ternaryOperation(insn: AbstractInsnNode, value1: MultValue, value2: MultValue, value3: MultValue): MultValue? = null

    override fun unaryOperation(insn: AbstractInsnNode, value: MultValue): MultValue? {
        val bv = basicInterpreter.unaryOperation(insn, value.basicValue)
        return when (insn.opcode) {
            Opcodes.GETFIELD -> getField(bv, insn as FieldInsnNode)
            Opcodes.PUTSTATIC -> {
                putField(insn as FieldInsnNode, value)
                null
            }
            else -> bv?.let { MultValue.Simple(it) }
        }
    }

    sealed class MultValue : Value {

        override fun getSize(): Int = basicValue.size

        abstract val basicValue: BasicValue

        data class Simple(override val basicValue: BasicValue) : MultValue()

        data class IntegerLdc(override val basicValue: BasicValue, val n: Number) : MultValue()

        data class GetField(override val basicValue: BasicValue, val insn: FieldInsnNode) : MultValue()

        data class GetFieldMult(override val basicValue: BasicValue, val insn: FieldInsnNode, val n: Number) : MultValue()

        data class LdcMult(override val basicValue: BasicValue, val n: Number) : MultValue()
    }
}
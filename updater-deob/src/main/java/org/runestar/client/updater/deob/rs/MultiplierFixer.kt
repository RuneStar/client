package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.LONG_TYPE
import org.objectweb.asm.tree.*
import org.objectweb.asm.tree.analysis.*
import org.runestar.client.updater.common.invert
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.*
import java.nio.file.Files
import java.nio.file.Path

object MultiplierFixer : Transformer {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)

        val multFile: Path = source.resolveSibling(source.fileName.toString() + ".mult.json")
        check(Files.exists(multFile))

        val decoders: Map<String, Long> = mapper.readValue(multFile.toFile())

        for (c in classNodes) {
            for (m in c.methods) {
                m.maxStack += 2 // todo
                cancelOutMultipliers(m, decoders)
                solveConstantMath(c, m)
                m.maxStack -= 2
            }
        }

        writeJar(classNodes, destination)
    }

    private fun cancelOutMultipliers(m: MethodNode, decoders: Map<String, Long>) {
        val insnList = m.instructions
        for (insn in insnList.iterator()) {
            if (insn !is FieldInsnNode) continue
            if (insn.desc != INT_TYPE.descriptor && insn.desc != LONG_TYPE.descriptor) continue
            val fieldName = "${insn.owner}.${insn.name}"
            val decoder = decoders[fieldName] ?: continue
            when (insn.opcode) {
                GETFIELD, GETSTATIC -> {
                    when (insn.desc) {
                        INT_TYPE.descriptor -> {
                            when (insn.next.opcode) {
                                I2L -> insnList.insertSafe(insn.next, LdcInsnNode(invert(decoder)), InsnNode(LMUL))
                                else -> insnList.insertSafe(insn, LdcInsnNode(invert(decoder.toInt())), InsnNode(IMUL))
                            }
                        }
                        LONG_TYPE.descriptor -> insnList.insertSafe(insn, LdcInsnNode(invert(decoder)), InsnNode(LMUL))
                        else -> error(insn)
                    }
                }
                PUTFIELD -> {
                    when (insn.desc) {
                        INT_TYPE.descriptor -> {
                            when (insn.previous.opcode) {
                                DUP_X1 -> {
                                    insnList.insertBeforeSafe(insn.previous, LdcInsnNode(decoder.toInt()), InsnNode(IMUL))
                                    insnList.insertSafe(insn, LdcInsnNode(invert(decoder.toInt())), InsnNode(IMUL))
                                }
                                DUP, DUP_X2, DUP2, DUP2_X1, DUP2_X2 -> error(insn)
                                else -> insnList.insertBeforeSafe(insn, LdcInsnNode(decoder.toInt()), InsnNode(IMUL))
                            }
                        }
                        LONG_TYPE.descriptor -> {
                            when (insn.previous.opcode) {
                                DUP2_X1 -> {
                                    insnList.insertBeforeSafe(insn.previous, LdcInsnNode(decoder), InsnNode(LMUL))
                                    insnList.insertSafe(insn, LdcInsnNode(invert(decoder)), InsnNode(LMUL))
                                }
                                DUP, DUP_X1, DUP_X2, DUP2, DUP2_X2 -> error(insn)
                                else -> insnList.insertBeforeSafe(insn, LdcInsnNode(decoder), InsnNode(LMUL))
                            }
                        }
                        else -> error(insn)
                    }
                }
                PUTSTATIC -> {
                    when (insn.desc) {
                        INT_TYPE.descriptor -> {
                            when (insn.previous.opcode) {
                                DUP -> {
                                    insnList.insertBeforeSafe(insn.previous, LdcInsnNode(decoder.toInt()), InsnNode(IMUL))
                                    insnList.insertSafe(insn, LdcInsnNode(invert(decoder.toInt())), InsnNode(IMUL))
                                }
                                DUP_X1, DUP_X2, DUP2, DUP2_X1, DUP2_X2 -> error(insn)
                                else -> insnList.insertBeforeSafe(insn, LdcInsnNode(decoder.toInt()), InsnNode(IMUL))
                            }
                        }
                        LONG_TYPE.descriptor -> {
                            when (insn.previous.opcode) {
                                DUP2 -> {
                                    insnList.insertBeforeSafe(insn.previous, LdcInsnNode(decoder), InsnNode(LMUL))
                                    insnList.insertSafe(insn, LdcInsnNode(invert(decoder)), InsnNode(LMUL))
                                }
                                DUP, DUP_X1, DUP_X2, DUP2_X1, DUP2_X2 -> error(insn)
                                else -> insnList.insertBeforeSafe(insn, LdcInsnNode(decoder), InsnNode(LMUL))
                            }
                        }
                        else -> error(insn)
                    }
                }
            }
        }
    }

    private fun solveConstantMath(c: ClassNode, m: MethodNode) {
        val insnList = m.instructions
        val interpreter = Inter()
        val analyzer = Analyzer(interpreter)
        analyzer.analyze(c.name, m)
        for (mul in interpreter.constantMultiplications) {
            when (mul.insn.opcode) {
                IMUL -> associateMultiplication(insnList, mul, 1)
                LMUL -> associateMultiplication(insnList, mul, 1L)
                else -> error(mul)
            }
        }
    }

    private fun associateMultiplication(insnList: InsnList, mul: Expr.Mul, num: Int) {
        if (mul.const.insn !in insnList) return
        val n = num * mul.const.n.toInt()
        val other = mul.other
        when {
            other is Expr.Mul -> {
                insnList.removeSafe(mul.insn, mul.const.insn)
                associateMultiplication(insnList, other, n)
            }
            other is Expr.Const -> {
                insnList.removeSafe(mul.insn, mul.const.insn)
                insnList.setSafe(other.insn, pushConstIntInsn(n * other.n.toInt()))
            }
            other is Expr.Add -> {
                insnList.removeSafe(mul.insn, mul.const.insn)
                distributeAddition(insnList, other.a, n)
                distributeAddition(insnList, other.b, n)
            }
            n == 1 -> insnList.removeSafe(mul.insn, mul.const.insn)
            else -> insnList.setSafe(mul.const.insn, pushConstIntInsn(n))
        }
    }

    private fun associateMultiplication(insnList: InsnList, mul: Expr.Mul, num: Long) {
        if (mul.const.insn !in insnList) return
        val n = num * mul.const.n.toLong()
        val other = mul.other
        when {
            other is Expr.Mul -> {
                insnList.removeSafe(mul.insn, mul.const.insn)
                associateMultiplication(insnList, other, n)
            }
            other is Expr.Const -> {
                insnList.removeSafe(mul.insn, mul.const.insn)
                insnList.setSafe(other.insn, pushConstLongInsn(n * other.n.toLong()))
            }
            other is Expr.Add -> {
                insnList.removeSafe(mul.insn, mul.const.insn)
                distributeAddition(insnList, other.a, n)
                distributeAddition(insnList, other.b, n)
            }
            n == 1L -> insnList.removeSafe(mul.insn, mul.const.insn)
            else -> insnList.setSafe(mul.const.insn, pushConstLongInsn(n))
        }
    }

    private fun distributeAddition(insnList: InsnList, expr: Expr, n: Int) {
        when (expr) {
            is Expr.Const -> insnList.setSafe(expr.insn, pushConstIntInsn(n * expr.n.toInt()))
            is Expr.Mul -> associateMultiplication(insnList, expr, n)
            else -> error(expr)
        }
    }

    private fun distributeAddition(insnList: InsnList, expr: Expr, n: Long) {
        when (expr) {
            is Expr.Const -> insnList.setSafe(expr.insn, pushConstLongInsn(n * expr.n.toLong()))
            is Expr.Mul -> associateMultiplication(insnList, expr, n)
            else -> error(expr)
        }
    }

    private class Inter : Interpreter<Expr>(ASM6) {

        private val sourceInterpreter = SourceInterpreter()

        private val mults = LinkedHashMap<AbstractInsnNode, Expr.Mul>()

        override fun binaryOperation(insn: AbstractInsnNode, value1: Expr, value2: Expr): Expr? {
            val bv = sourceInterpreter.binaryOperation(insn, value1.sv, value2.sv) ?: return null
            if (value1 == value2) return Expr.Var(bv)
            return when (insn.opcode) {
                IMUL, LMUL -> {
                    if (value1 !is Expr.Const && value2 !is Expr.Const) {
                        Expr.Var(bv)
                    } else {
                        Expr.Mul(bv, value1, value2).also {
                            mults[insn] = it
                        }
                    }
                }
                IADD, ISUB, LADD, LSUB -> {
                    if ((value1 is Expr.Const || value1 is Expr.Mul) && (value2 is Expr.Const || value2 is Expr.Mul)) {
                        Expr.Add(bv, value1, value2)
                    } else {
                        Expr.Var(bv)
                    }
                }
                else -> Expr.Var(bv)
            }
        }

        override fun copyOperation(insn: AbstractInsnNode, value: Expr): Expr = value

        override fun merge(value1: Expr, value2: Expr): Expr {
            if (value1 == value2) {
                return value1
            } else if (value1 is Expr.Mul && value2 is Expr.Mul && value1.insn == value2.insn) {
                if (value1.a == value2.a && value1.a is Expr.Const) {
                    return Expr.Mul(value1.sv, value1.a, merge(value1.b, value2.b)).also { mults[value1.insn] = it }
                } else if (value1.b == value2.b && value1.b is Expr.Const) {
                    return Expr.Mul(value1.sv, merge(value1.a, value2.a), value1.b).also { mults[value1.insn] = it }
                }
            } else if (value1 is Expr.Add && value2 is Expr.Add && value1.insn == value2.insn) {
                if (value1.a == value2.a && value1.a !is Expr.Var) {
                    val bb = merge(value1.b, value2.b)
                    if (bb is Expr.Const || bb is Expr.Mul) {
                        return Expr.Add(value1.sv, value1.a, bb)
                    }
                } else if (value1.b == value2.b && value2.b !is Expr.Var) {
                    val aa = merge(value1.a, value2.a)
                    if (aa is Expr.Const || aa is Expr.Mul) {
                        return Expr.Add(value1.sv, aa, value1.b)
                    }
                }
            }
            return Expr.Var(sourceInterpreter.merge(value1.sv, value2.sv))
        }

        override fun naryOperation(insn: AbstractInsnNode, values: MutableList<out Expr>): Expr? {
            return sourceInterpreter.naryOperation(insn, emptyList())?.let { Expr.Var(it) }
        }

        override fun newOperation(insn: AbstractInsnNode): Expr {
            val bv = sourceInterpreter.newOperation(insn)
            return when (insn.opcode) {
                LDC ->  {
                    val cst = (insn as LdcInsnNode).cst
                    when (cst) {
                        is Int, is Long -> Expr.Const(bv, cst as Number)
                        else -> Expr.Var(bv)
                    }
                }
                ICONST_1, LCONST_1 -> Expr.Const(bv, 1)
                ICONST_0, LCONST_0 -> Expr.Const(bv, 0)
                else -> Expr.Var(bv)
            }
        }

        override fun newValue(type: Type?): Expr? {
            return sourceInterpreter.newValue(type)?.let { Expr.Var(it) }
        }

        override fun returnOperation(insn: AbstractInsnNode, value: Expr, expected: Expr) {}

        override fun ternaryOperation(insn: AbstractInsnNode, value1: Expr, value2: Expr, value3: Expr): Expr? = null

        override fun unaryOperation(insn: AbstractInsnNode, value: Expr): Expr? {
            return sourceInterpreter.unaryOperation(insn, value.sv)?.let { Expr.Var(it) }
        }

        val constantMultiplications: Collection<Expr.Mul> get() {
            val ms = LinkedHashSet<Expr.Mul>()
            for (m in mults.values) {
                val other = m.other
                if (other is Expr.Mul) {
                    ms.remove(other)
                }
                if (other is Expr.Add && other.a is Expr.Mul) {
                    ms.remove(other.a)
                }
                if (other is Expr.Add && other.b is Expr.Mul) {
                    ms.remove(other.b)
                }
                ms.add(m)
            }
            return ms
        }
    }

    private sealed class Expr : Value {

        override fun getSize(): Int = sv.size

        abstract val sv: SourceValue

        val insn get() = sv.insns.single()

        data class Var(override val sv: SourceValue) : Expr() {

            override fun toString(): String = "(#${sv.hashCode().toString(16)})"
        }

        data class Const(override val sv: SourceValue, val n: Number) : Expr() {

            override fun toString(): String ="($n)"
        }

        data class Add(override val sv: SourceValue, val a: Expr, val b: Expr) : Expr() {

            override fun toString(): String {
                val c = if (insn.opcode == IADD || insn.opcode == LADD) '+' else '-'
                return "($a$c$b)"
            }
        }

        data class Mul(override val sv: SourceValue, val a: Expr, val b: Expr) : Expr() {

            val const get() = a as? Const ?: b as Const

            val other get() = if (const == a) b else a

            override fun toString(): String = "($a*$b)"
        }
    }
}
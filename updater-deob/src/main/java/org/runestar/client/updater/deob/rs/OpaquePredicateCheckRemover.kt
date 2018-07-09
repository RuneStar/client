package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import java.lang.reflect.Modifier
import java.nio.file.Path
import java.util.*

object OpaquePredicateCheckRemover : Transformer {

    private val ISE_INTERNAL_NAME = Type.getInternalName(IllegalStateException::class.java)

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        val passingArgs = TreeMap<String, Int>()
        var returns = 0
        var exceptions = 0
        classNodes.forEach { c ->
            c.methods.forEach { m ->
                val instructions = m.instructions.iterator()
                val lastParamIndex = m.lastParamIndex
                while (instructions.hasNext()) {
                    val insn = instructions.next()
                    val toDelete = if (insn.matchesReturn(lastParamIndex)) {
                        returns++
                        4
                    } else if (insn.matchesException(lastParamIndex)) {
                        exceptions++
                        7
                    } else {
                        continue
                    }
                    val constantPushed = insn.next.constantIntProduced
                    val ifOpcode = insn.next.next.opcode
                    val label = (insn.next.next as JumpInsnNode).label.label
                    instructions.remove()
                    repeat(toDelete - 1) {
                        instructions.next()
                        instructions.remove()
                    }
                    instructions.add(JumpInsnNode(GOTO, LabelNode(label)))
                    passingArgs[c.name + "." + m.name + m.desc] = passingVal(constantPushed, ifOpcode)
                }
            }
        }
        logger.info { "Opaque predicates checks removed: returns: $returns, exceptions: $exceptions" }
        val opFile = destination.resolveSibling(destination.fileName.toString() + ".op.json").toFile()
        mapper.writeValue(opFile, passingArgs)
        writeJar(classNodes, destination)
    }

    private fun AbstractInsnNode.matchesReturn(lastParamIndex: Int): Boolean {
        val i0 = this
        if (i0.opcode != ILOAD) return false
        i0 as VarInsnNode
        if (i0.`var` != lastParamIndex) return false
        val i1 = i0.next
        if (!i1.isConstantIntProducer) return false
        val i2 = i1.next
        if (!i2.isIf) return false
        val i3 = i2.next
        if (!i3.isReturn) return false
        return true
    }

    private fun AbstractInsnNode.matchesException(lastParamIndex: Int): Boolean {
        val i0 = this
        if (i0.opcode != ILOAD) return false
        i0 as VarInsnNode
        if (i0.`var` != lastParamIndex) return false
        val i1 = i0.next
        if (!i1.isConstantIntProducer) return false
        val i2 = i1.next
        if (!i2.isIf) return false
        val i3 = i2.next
        if (i3.opcode != NEW) return false
        val i4 = i3.next
        if (i4.opcode != DUP) return false
        val i5 = i4.next
        if (i5.opcode != INVOKESPECIAL) return false
        i5 as MethodInsnNode
        if (i5.owner != ISE_INTERNAL_NAME) return false
        val i6 = i5.next
        if (i6.opcode != ATHROW) return false
        return true
    }

    private val MethodNode.lastParamIndex: Int get() {
        val offset = if (Modifier.isStatic(access)) 1 else 0
        return (Type.getArgumentsAndReturnSizes(desc) shr 2) - offset - 1
    }

    private fun passingVal(pushed: Int, ifOpcode: Int): Int {
        return when(ifOpcode) {
            IF_ICMPEQ -> pushed
            IF_ICMPGE,
            IF_ICMPGT -> pushed + 1
            IF_ICMPLE,
            IF_ICMPLT,
            IF_ICMPNE -> pushed - 1
            else -> error(ifOpcode)
        }
    }

    private val AbstractInsnNode.isConstantIntProducer: Boolean get() {
        return when (opcode) {
            LDC -> (this as LdcInsnNode).cst is Int
            SIPUSH, BIPUSH, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5, ICONST_M1 -> true
            else -> false
        }
    }

    private val AbstractInsnNode.constantIntProduced: Int get() {
        return when (opcode) {
            LDC -> (this as LdcInsnNode).cst as Int
            SIPUSH, BIPUSH -> (this as IntInsnNode).operand
            ICONST_0 -> 0
            ICONST_1 -> 1
            ICONST_2 -> 2
            ICONST_3 -> 3
            ICONST_4 -> 4
            ICONST_5 -> 5
            ICONST_M1 -> -1
            else -> error(this)
        }
    }

    private val AbstractInsnNode.isIf: Boolean get() {
        return this is JumpInsnNode && opcode != Opcodes.GOTO
    }

    private val AbstractInsnNode.isReturn: Boolean get() {
        return when (opcode) {
            RETURN, ARETURN, DRETURN, FRETURN, IRETURN, LRETURN -> true
            else -> false
        }
    }
}
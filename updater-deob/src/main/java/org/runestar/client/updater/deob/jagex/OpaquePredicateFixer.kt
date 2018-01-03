package org.runestar.client.updater.deob.jagex

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.deob.writeJar
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*
import org.objectweb.asm.util.Printer
import java.lang.reflect.Modifier
import java.nio.file.Path
import java.util.*

object OpaquePredicateFixer : Transformer {

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
                    instructions.add(JumpInsnNode(Opcodes.GOTO, LabelNode(label)))
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
        if (i0.opcode != Opcodes.ILOAD) return false
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
        if (i0.opcode != Opcodes.ILOAD) return false
        i0 as VarInsnNode
        if (i0.`var` != lastParamIndex) return false
        val i1 = i0.next
        if (!i1.isConstantIntProducer) return false
        val i2 = i1.next
        if (!i2.isIf) return false
        val i3 = i2.next
        if (i3.opcode != Opcodes.NEW) return false
        val i4 = i3.next
        if (i4.opcode != Opcodes.DUP) return false
        val i5 = i4.next
        if (i5.opcode != Opcodes.INVOKESPECIAL) return false
        i5 as MethodInsnNode
        if (i5.owner != Type.getInternalName(IllegalStateException::class.java)) return false
        val i6 = i5.next
        if (i6.opcode != Opcodes.ATHROW) return false
        return true
    }

    private val MethodNode.lastParamIndex: Int get() {
        val offset = if (Modifier.isStatic(access)) 1 else 0
        return (Type.getArgumentsAndReturnSizes(desc) shr 2) - offset - 1
    }

    private fun passingVal(pushed: Int, ifOpcode: Int): Int {
        return when(ifOpcode) {
            Opcodes.IF_ICMPEQ -> pushed
            Opcodes.IF_ICMPGE,
            Opcodes.IF_ICMPGT -> pushed + 1
            Opcodes.IF_ICMPLE,
            Opcodes.IF_ICMPLT,
            Opcodes.IF_ICMPNE -> pushed - 1
            else -> error(ifOpcode)
        }
    }

    private val AbstractInsnNode.isConstantIntProducer: Boolean get() {
        return when (opcode) {
            -1 -> false
            Opcodes.LDC -> (this as LdcInsnNode).cst is Int
            else -> Printer.OPCODES[opcode].let { it.endsWith("IPUSH") || it.startsWith("ICONST_") }
        }
    }

    private val AbstractInsnNode.constantIntProduced: Int get() {
        return when (opcode) {
            Opcodes.LDC -> (this as LdcInsnNode).cst as Int
            -1 -> error(-1)
            else -> Printer.OPCODES[opcode].let {
                if (it.endsWith("IPUSH")) {
                    this as IntInsnNode
                    operand
                } else {
                    val suf = it.removePrefix("ICONST_")
                    suf.replace('M', '-').toInt()
                }
            }
        }
    }

    val AbstractInsnNode.isIf: Boolean get() {
        return this is JumpInsnNode && opcode != Opcodes.GOTO
    }

    val AbstractInsnNode.isReturn: Boolean get() {
        return opcode >= 0 && Printer.OPCODES[opcode].endsWith("RETURN")
    }
}
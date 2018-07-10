package org.runestar.client.updater.deob.util

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.IntInsnNode
import org.objectweb.asm.tree.LdcInsnNode

fun pushConstIntInsn(n: Int): AbstractInsnNode {
    return when (n) {
        0 -> InsnNode(Opcodes.ICONST_0)
        1 -> InsnNode(Opcodes.ICONST_1)
        2 -> InsnNode(Opcodes.ICONST_2)
        3 -> InsnNode(Opcodes.ICONST_3)
        4 -> InsnNode(Opcodes.ICONST_4)
        5 -> InsnNode(Opcodes.ICONST_5)
        -1 -> InsnNode(Opcodes.ICONST_M1)
        in Byte.MIN_VALUE..Byte.MAX_VALUE -> IntInsnNode(Opcodes.BIPUSH, n)
        in Short.MIN_VALUE..Short.MAX_VALUE -> IntInsnNode(Opcodes.SIPUSH, n)
        else -> LdcInsnNode(n)
    }
}

fun pushConstLongInsn(n: Long): AbstractInsnNode {
    return when (n) {
        0L -> InsnNode(Opcodes.LCONST_0)
        1L -> InsnNode(Opcodes.LCONST_1)
        else -> LdcInsnNode(n)
    }
}

val AbstractInsnNode.isConstantIntProducer: Boolean get() {
    return when (opcode) {
        Opcodes.LDC -> (this as LdcInsnNode).cst is Int
        Opcodes.SIPUSH, Opcodes.BIPUSH, Opcodes.ICONST_0, Opcodes.ICONST_1, Opcodes.ICONST_2, Opcodes.ICONST_3, Opcodes.ICONST_4, Opcodes.ICONST_5, Opcodes.ICONST_M1 -> true
        else -> false
    }
}

val AbstractInsnNode.constantIntProduced: Int get() {
    return when (opcode) {
        Opcodes.LDC -> (this as LdcInsnNode).cst as Int
        Opcodes.SIPUSH, Opcodes.BIPUSH -> (this as IntInsnNode).operand
        Opcodes.ICONST_0 -> 0
        Opcodes.ICONST_1 -> 1
        Opcodes.ICONST_2 -> 2
        Opcodes.ICONST_3 -> 3
        Opcodes.ICONST_4 -> 4
        Opcodes.ICONST_5 -> 5
        Opcodes.ICONST_M1 -> -1
        else -> error(this)
    }
}
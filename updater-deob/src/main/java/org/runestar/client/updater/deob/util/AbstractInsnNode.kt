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
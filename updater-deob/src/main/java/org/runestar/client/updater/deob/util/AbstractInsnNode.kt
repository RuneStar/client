package org.runestar.client.updater.deob.util

import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.IntInsnNode
import org.objectweb.asm.tree.LdcInsnNode

fun loadInt(n: Int): AbstractInsnNode = when (n) {
    in -1..5 -> InsnNode(n + 3)
    in Byte.MIN_VALUE..Byte.MAX_VALUE -> IntInsnNode(BIPUSH, n)
    in Short.MIN_VALUE..Short.MAX_VALUE -> IntInsnNode(SIPUSH, n)
    else -> LdcInsnNode(n)
}

fun loadLong(n: Long): AbstractInsnNode = when (n) {
    0L, 1L -> InsnNode((n + 9).toInt())
    else -> LdcInsnNode(n)
}

val AbstractInsnNode.isIntValue: Boolean get() {
    return when (opcode) {
        LDC -> (this as LdcInsnNode).cst is Int
        SIPUSH, BIPUSH, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5, ICONST_M1 -> true
        else -> false
    }
}

val AbstractInsnNode.intValue: Int get() {
    if (opcode in 2..8) return opcode - 3
    if (opcode == BIPUSH || opcode == SIPUSH) return (this as IntInsnNode).operand
    if (this is LdcInsnNode && cst is Int) return cst as Int
    error(this)
}
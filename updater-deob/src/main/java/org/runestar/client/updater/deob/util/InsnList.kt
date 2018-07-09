package org.runestar.client.updater.deob.util

import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList

fun InsnList.insertSafe(previousInsn: AbstractInsnNode, vararg insns: AbstractInsnNode) {
    check(contains(previousInsn))
    insns.reversed().forEach { insert(previousInsn, it) }
}

fun InsnList.insertBeforeSafe(nextInsn: AbstractInsnNode, vararg insns: AbstractInsnNode) {
    check(contains(nextInsn))
    insns.forEach { insertBefore(nextInsn, it) }
}

fun InsnList.removeSafe(vararg insns: AbstractInsnNode) {
    insns.forEach {
        check(contains(it))
        remove(it)
    }
}

fun InsnList.setSafe(oldInsn: AbstractInsnNode, newInsn: AbstractInsnNode) {
    check(contains(oldInsn))
    set(oldInsn, newInsn)
}
package com.runesuite.client.updater.deob.extensions

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import org.objectweb.asm.util.Printer

val AbstractInsnNode.switchLabelNodes: List<LabelNode> get() {
    return when (this) {
        is LookupSwitchInsnNode -> this.labels
        is TableSwitchInsnNode -> this.labels
        else -> emptyList()
    }
}

val AbstractInsnNode.isSwitch: Boolean get() {
    return this is LookupSwitchInsnNode || this is TableSwitchInsnNode
}

val AbstractInsnNode.isBranch: Boolean get() {
    return this is JumpInsnNode || isSwitch
}

val AbstractInsnNode.isIf: Boolean get() {
    return this is JumpInsnNode && opcode != Opcodes.GOTO
}

val AbstractInsnNode.branchLabelNodes: List<LabelNode> get() {
    return if (this is JumpInsnNode) {
        listOf(this.label)
    } else if (isSwitch) {
        switchLabelNodes
    } else {
        emptyList()
    }
}

val AbstractInsnNode.isReturn: Boolean get() {
    return opcode >= 0 && Printer.OPCODES[opcode].endsWith("RETURN")
}


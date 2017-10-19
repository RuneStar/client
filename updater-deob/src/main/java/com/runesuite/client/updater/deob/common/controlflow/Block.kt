package com.runesuite.client.updater.deob.common.controlflow

import org.objectweb.asm.tree.AbstractInsnNode

class Block {

    val instructions = LinkedHashSet<AbstractInsnNode>()

    var immediateSuccessor: Block? = null

    var immediatePredecessor: Block? = null

    val immediateOrigin: Block get() {
        var b = this
        var pred = immediatePredecessor
        while (pred != null) {
            b = pred
            pred = b.immediatePredecessor
        }
        return b
    }

    val branchSuccessors = HashSet<Block>()
}
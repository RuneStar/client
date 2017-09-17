package com.runesuite.client.updater.deob.common.controlflow

import com.runesuite.client.updater.deob.extensions.branchLabelNodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.LabelNode

class Block {

    val predecessors: MutableSet<Block> = HashSet()
    val successors: MutableSet<Block> = HashSet()
    val instructions: MutableList<AbstractInsnNode> = ArrayList()

    val immediateSuccessor: Block? get() {
        return successors.firstOrNull {
            !branchLabelNodes.contains(it.instructions.first())
        }
    }

    val immediatePredecessor: Block? get() {
        return predecessors.firstOrNull {
            !it.branchLabelNodes.contains(instructions.first())
        }
    }

    val immediateOrigin: Block get() {
        var b = this
        var pred = immediatePredecessor
        while (pred != null) {
            b = pred
            pred = b.immediatePredecessor
        }
        return b
    }

    val branchSuccessors: Set<Block> get() {
        return successors.filter { branchLabelNodes.contains(it.instructions.first()) }
                .toSet()
    }

    private val branchLabelNodes: List<LabelNode> get() {
        return instructions.last().branchLabelNodes
    }
}
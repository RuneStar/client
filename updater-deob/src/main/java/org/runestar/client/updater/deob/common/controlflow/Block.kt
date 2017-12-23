package org.runestar.client.updater.deob.common.controlflow

class Block {

    // inclusive
    var instructionsStart = 0

    // exclusive
    var instructionsEnd = 0

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

    val branchSuccessors = ArrayList<Block>()
}
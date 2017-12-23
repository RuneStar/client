package org.runestar.client.updater.deob.common.controlflow

import org.objectweb.asm.tree.*
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.objectweb.asm.tree.analysis.BasicValue
import java.util.*

class BlockAnalyzer : Analyzer<BasicValue>(BasicInterpreter()) {

    val blocks = ArrayList<Block>()

    override fun init(owner: String, m: MethodNode) {
        val insnList = m.instructions
        var currentBlock = Block()
        blocks.add(currentBlock)
        for (i in 0 until insnList.size()) {
            val insn = insnList[i]
            currentBlock.instructionsEnd++
            if (insn.next == null) break
            if (insn.next.type == AbstractInsnNode.LABEL ||
                    insn.type == AbstractInsnNode.JUMP_INSN ||
                    insn.type == AbstractInsnNode.LOOKUPSWITCH_INSN ||
                    insn.type == AbstractInsnNode.TABLESWITCH_INSN) {
                currentBlock = Block()
                currentBlock.instructionsStart = i + 1
                currentBlock.instructionsEnd = i + 1
                blocks.add(currentBlock)
            }
        }
    }

    override fun newControlFlowEdge(insn: Int, successor: Int) {
        val b1 = findBlock(insn)
        val b2 = findBlock(successor)
        if (b1 != b2) {
            if (insn + 1 == successor) {
                b1.immediateSuccessor = b2
                b2.immediatePredecessor = b1
            } else {
                b1.branchSuccessors.add(b2)
            }
        }
    }

    private fun findBlock(insnIndex: Int): Block {
        return blocks.first { insnIndex in it.instructionsStart until it.instructionsEnd }
    }
}
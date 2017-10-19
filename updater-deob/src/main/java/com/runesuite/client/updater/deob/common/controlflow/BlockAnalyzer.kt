package com.runesuite.client.updater.deob.common.controlflow

import org.objectweb.asm.tree.*
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.objectweb.asm.tree.analysis.BasicValue

class BlockAnalyzer : Analyzer<BasicValue>(BasicInterpreter()) {

    lateinit var instructions: List<AbstractInsnNode>

    var blocks: MutableList<Block> = ArrayList()

    override fun init(owner: String, m: MethodNode) {
        instructions = m.instructions.toArray().asList()
        var b = Block()
        blocks.add(b)
        var ain = instructions.first()
        b.instructions.add(ain)
        while (ain.next != null) {
            ain = ain.next
            b.instructions.add(ain)
            if (ain.next == null) break
            if (ain.next is LabelNode || ain is JumpInsnNode || ain is LookupSwitchInsnNode || ain is TableSwitchInsnNode) {
                b = Block()
                blocks.add(b)
            }
        }
    }

    override fun newControlFlowEdge(insn: Int, successor: Int) {
        val b1 = findBlock(instructions[insn])
        val b2 = findBlock(instructions[successor])
        if (b1 != b2) {
            if (insn + 1 == successor) {
                b1.immediateSuccessor = b2
                b2.immediatePredecessor = b1
            } else {
                b1.branchSuccessors.add(b2)
            }
        }
    }

    private fun findBlock(abstractInsnNode: AbstractInsnNode): Block {
        return blocks.first { it.instructions.contains(abstractInsnNode) }
    }
}
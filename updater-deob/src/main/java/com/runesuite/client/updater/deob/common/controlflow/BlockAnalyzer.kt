package com.runesuite.client.updater.deob.common.controlflow

import com.runesuite.client.updater.deob.extensions.isBranch
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.objectweb.asm.tree.analysis.BasicValue

class BlockAnalyzer : Analyzer<BasicValue>(BasicInterpreter()) {

    lateinit var instructions: List<AbstractInsnNode>

    var blocks: MutableList<Block> = ArrayList()

    override fun init(owner: String, m: MethodNode) {
        instructions = m.instructions.toArray().toList()
        var b = Block()
        blocks.add(b)
        var ain = instructions.first()
        b.instructions.add(ain)
        while (ain.next != null) {
            ain = ain.next
            b.instructions.add(ain)
            if (ain.next != null && (ain.next is LabelNode || ain.isBranch)) {
                b = Block()
                blocks.add(b)
            }
        }
    }

    override fun newControlFlowEdge(insn: Int, successor: Int) {
        val b1 = findBlock(instructions[insn])
        val b2 = findBlock(instructions[successor])
        if (b1 != b2) {
            b1.successors.add(b2)
            b2.predecessors.add(b1)
        }
    }

    private fun findBlock(abstractInsnNode: AbstractInsnNode): Block {
        return blocks.first { it.instructions.contains(abstractInsnNode) }
    }
}
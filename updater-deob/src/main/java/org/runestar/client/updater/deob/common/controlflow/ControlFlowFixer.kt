package org.runestar.client.updater.deob.common.controlflow

import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.LabelNode
import org.runestar.client.updater.deob.Transformer
import java.nio.file.Path
import java.util.AbstractMap
import java.util.ArrayDeque
import java.util.Collections
import java.util.Queue

object ControlFlowFixer : Transformer.Tree() {

    private val logger = getLogger()

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        var blockCount = 0
        klasses.forEach { k ->
            k.methods.forEach { m ->
                if (m.tryCatchBlocks.isEmpty()) {
                    val analyzer = BlockAnalyzer()
                    analyzer.analyze(k.name, m)
                    m.instructions = buildInsnList(m.instructions, analyzer.blocks)
                    blockCount += analyzer.blocks.size
                }
            }
        }
        logger.info { "Blocks reordered: $blockCount" }
    }

    private fun buildInsnList(originalInstructions: InsnList, blocks: List<Block>): InsnList {
        val instructions = InsnList()
        if (blocks.isEmpty()) {
            return instructions
        }
        val labelMap = LabelMap()
        val stack: Queue<Block> = Collections.asLifoQueue(ArrayDeque())
        stack.add(blocks.first())
        val placed = HashSet<Block>()
        while (stack.isNotEmpty()) {
            val b = stack.remove()
            if (b in placed) continue
            placed.add(b)
            b.branchSuccessors.forEach { stack.add(it.immediateOrigin) }
            b.immediateSuccessor?.let { stack.add(it) }
            for (i in b.instructionsStart until b.instructionsEnd) {
                instructions.add(originalInstructions[i].clone(labelMap))
            }
        }
        return instructions
    }

    private class LabelMap : AbstractMap<LabelNode, LabelNode>() {
        private val map = HashMap<LabelNode, LabelNode>()
        override val entries get() = throw IllegalStateException()
        override fun get(key: LabelNode) = map.getOrPut(key) { LabelNode() }
    }
}
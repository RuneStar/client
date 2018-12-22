package org.runestar.client.updater.deob.common.controlflow

import org.kxtra.slf4j.info
import org.kxtra.slf4j.getLogger
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.LabelNode
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import java.nio.file.Path
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object ControlFlowFixer : Transformer {

    private val logger = getLogger()

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        var blockCount = 0
        classNodes.forEach { c ->
            c.methods.forEach { m ->
                if (m.tryCatchBlocks.isEmpty()) {
                    val analyzer = BlockAnalyzer()
                    analyzer.analyze(c.name, m)
                    blockCount += analyzer.blocks.size
                    m.instructions = buildInsnList(m.instructions, analyzer.blocks)
                }
            }
        }
        logger.info { "Blocks reordered: $blockCount" }
        writeJar(classNodes, destination)
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
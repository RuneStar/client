package com.runesuite.client.updater.deob.common.controlflow

import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.readJar
import com.runesuite.client.updater.deob.writeJar
import mu.KotlinLogging
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.LabelNode
import java.nio.file.Path
import java.util.*
import kotlin.collections.HashSet

object ControlFlowFixer : Deobfuscator {

    private val logger = KotlinLogging.logger { }

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source)
        var blockCount = 0
        classNodes.forEach { c ->
            c.methods.forEach { m ->
                if (m.tryCatchBlocks.isEmpty()) {
                    val analyzer = BlockAnalyzer()
                    analyzer.analyze(c.name, m)
                    blockCount += analyzer.blocks.size
                    m.instructions.resetLabels()
                    m.instructions = buildInsnList(analyzer.blocks)
                }
            }
        }
        logger.debug { "Blocks reordered: $blockCount" }
        writeJar(classNodes, destination)
    }

    private fun buildInsnList(blocks: List<Block>): InsnList {
        val labels = blocks.flatMap { it.instructions }
                .mapNotNull { it as? LabelNode }
                .associate { it to it }
        val instructions = InsnList()
        if (blocks.isEmpty()) {
            return instructions
        }
        val stack: Queue<Block> = Collections.asLifoQueue(ArrayDeque())
        stack.add(blocks.first())
        val placed: MutableSet<Block> = HashSet()
        while (stack.isNotEmpty()) {
            val b = stack.remove()
            if (b in placed) continue
            placed.add(b)
            b.branchSuccessors.forEach { stack.add(it.immediateOrigin) }
            b.immediateSuccessor?.let { stack.add(it) }
            b.instructions.forEach { instructions.add(it.clone(labels)) }
        }
        return instructions
    }
}
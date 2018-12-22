package org.runestar.client.updater.deob.rs

import org.kxtra.slf4j.info
import org.kxtra.slf4j.getLogger
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import java.nio.file.Path

/**
 * Removes all try-catch blocks for [RuntimeException]s
 */
object UnusedTryCatchRemover : Transformer {

    private val logger = getLogger()

    private val RUNTIME_EXCEPTION_INTERNAL_NAME = Type.getInternalName(RuntimeException::class.java)

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        var removedTryCatches = 0
        classNodes.forEach { c ->
            c.methods.forEach { m ->
                val runtimes = m.tryCatchBlocks.filter { it.type == RUNTIME_EXCEPTION_INTERNAL_NAME }
                val runtimeHandlers = runtimes.map { it.handler }.distinct()
                runtimeHandlers.forEach { ln ->
                    val insns = m.instructions.iterator(m.instructions.indexOf(ln))
                    var insn: AbstractInsnNode
                    do {
                        insn = insns.next()
                        insns.remove()
                    } while (insn.opcode != Opcodes.ATHROW)
                    removedTryCatches++
                }
                m.tryCatchBlocks.removeAll(runtimes)
                val removedRanges = runtimes.map { it.start to it.end }
                m.tryCatchBlocks.removeIf { it.start to it.end in removedRanges }
            }
        }
        logger.info { "Try catches removed: $removedTryCatches" }
        writeJar(classNodes, destination)
    }
}
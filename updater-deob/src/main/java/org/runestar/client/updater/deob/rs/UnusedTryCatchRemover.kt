package org.runestar.client.updater.deob.rs

import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.common.RemoveDeadCode
import java.nio.file.Path

object UnusedTryCatchRemover : Transformer.Tree() {

    private val logger = getLogger()

    private val RUNTIME_EXCEPTION_INTERNAL_NAME = Type.getInternalName(RuntimeException::class.java)

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        var removedTryCatches = 0
        klasses.forEach { k ->
            k.methods.forEach { m ->
                val size = m.tryCatchBlocks.size
                m.tryCatchBlocks.removeIf { it.type == RUNTIME_EXCEPTION_INTERNAL_NAME }
                removedTryCatches += size - m.tryCatchBlocks.size
            }
        }
        logger.info { "Try catches removed: $removedTryCatches" }
        RemoveDeadCode.transform(dir, klasses)
    }
}
package org.runestar.client.updater.deob.common

import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import org.kxtra.slf4j.info
import org.kxtra.slf4j.getLogger
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import java.nio.file.Path

object UnnecessaryGotoRemover : Transformer {

    private val logger = getLogger()

    // todo : work without removing frames first

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        var gotosRemoved = 0
        classNodes.flatMap { it.methods }.forEach { m ->
            val instructions = m.instructions.iterator()
            while (instructions.hasNext()) {
                val insn0 = instructions.next()
                if (insn0.opcode != Opcodes.GOTO) continue
                insn0 as JumpInsnNode
                val insn1 = insn0.next
                if (insn1 == null || insn1 !is LabelNode) continue
                if (insn0.label == insn1) {
                    instructions.remove()
                    gotosRemoved++
                }
            }
        }
        logger.info { "Gotos removed: $gotosRemoved" }
        writeJar(classNodes, destination)
    }
}
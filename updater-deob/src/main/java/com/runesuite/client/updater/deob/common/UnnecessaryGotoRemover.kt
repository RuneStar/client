package com.runesuite.client.updater.deob.common

import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.readJar
import com.runesuite.client.updater.deob.writeJar
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import java.nio.file.Path

object UnnecessaryGotoRemover : Deobfuscator {

    private val logger = getLogger()

    // todo : work without removing frames first

    override fun deob(source: Path, destination: Path) {
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
package org.runestar.client.updater.deob.jagex

import org.runestar.client.updater.deob.Deobfuscator
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.deob.writeJar
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import java.nio.file.Path

object UnusedTryCatchRemover : Deobfuscator {

    private val logger = getLogger()

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source)
        var removedTryCatches = 0
        classNodes.flatMap { it.methods }.forEach { m ->
            val tcbs = m.tryCatchBlocks.iterator()
            while (tcbs.hasNext()) {
                val tcb = tcbs.next()
                if (tcb.type == Type.getInternalName(RuntimeException::class.java)) {
                    val insns = m.instructions.iterator(m.instructions.indexOf(tcb.handler))
                    var insn: AbstractInsnNode
                    do {
                        insn = insns.next()
                        insns.remove()
                    } while (insn.opcode != Opcodes.ATHROW)
                    tcbs.remove()
                    removedTryCatches++
                }
            }
        }
        logger.info { "Try catches removed: $removedTryCatches" }
        writeJar(classNodes, destination)
    }
}
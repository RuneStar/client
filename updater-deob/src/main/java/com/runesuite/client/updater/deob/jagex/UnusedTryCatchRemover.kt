package com.runesuite.client.updater.deob.jagex

import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.readJar
import com.runesuite.client.updater.deob.writeJar
import mu.KotlinLogging
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import java.nio.file.Path

object UnusedTryCatchRemover : Deobfuscator {

    private val logger = KotlinLogging.logger { }

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source)
        var removedTryCatches = 0
        classNodes.flatMap { it.methods }.forEach { m ->
            val tcbs = m.tryCatchBlocks.iterator()
            while (tcbs.hasNext()) {
                val tcb = tcbs.next()
                if (tcb.type == Type.getInternalName(RuntimeException::class.java)) {
                    m.instructions.resetLabels()
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
        logger.debug { "Try catches removed: $removedTryCatches" }
        writeJar(classNodes, destination)
    }
}
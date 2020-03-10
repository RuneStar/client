package org.runestar.client.updater.deob.rs

import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.ATHROW
import org.objectweb.asm.Opcodes.DUP
import org.objectweb.asm.Opcodes.INVOKESPECIAL
import org.objectweb.asm.Opcodes.NEW
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode
import org.runestar.client.updater.deob.Transformer
import java.nio.file.Path

/**
 * Removes constructors that contain only `throw new Error();`
 */
object RemoveErrorConstructors : Transformer.Tree() {

    private val EXCEPTIONS_LIST = listOf(Type.getType(Throwable::class.java).internalName)

    private val logger = getLogger()

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        var removedCount = 0

        klasses.forEach { c ->
            val methods = c.methods.iterator()
            while (methods.hasNext()) {
                val m = methods.next()
                if (isErrorConstructor(m)) {
                    methods.remove()
                    removedCount++
                }
            }
        }

        logger.info { "Constructors removed: $removedCount" }
    }

    private fun isErrorConstructor(m: MethodNode): Boolean {
        if (m.name != "<init>") return false
        if (Type.getArgumentTypes(m.desc).isNotEmpty()) return false
        if (m.exceptions != EXCEPTIONS_LIST) return false
        val insns = m.instructions.toArray().filter { it.opcode > 0 }.iterator()
        if (!insns.hasNext() || insns.next().opcode != ALOAD) return false
        if (!insns.hasNext() || insns.next().opcode != INVOKESPECIAL) return false
        if (!insns.hasNext() || insns.next().opcode != NEW) return false
        if (!insns.hasNext() || insns.next().opcode != DUP) return false
        if (!insns.hasNext() || insns.next().opcode != INVOKESPECIAL) return false
        if (!insns.hasNext() || insns.next().opcode != ATHROW) return false
        return !insns.hasNext()
    }
}
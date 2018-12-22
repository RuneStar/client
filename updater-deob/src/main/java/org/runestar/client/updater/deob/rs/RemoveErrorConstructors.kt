package org.runestar.client.updater.deob.rs

import org.kxtra.slf4j.info
import org.kxtra.slf4j.getLogger
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import java.nio.file.Path

/**
 * Removes constructors that contain only `throw new Error();`
 */
object RemoveErrorConstructors : Transformer {

    private val EXCEPTIONS_LIST = listOf(Type.getType(Throwable::class.java).internalName)

    private val logger = getLogger()

    override fun transform(source: Path, destination: Path) {
        val classNodes: Collection<ClassNode> = readJar(source)

        var removedCount = 0

        classNodes.forEach { c ->
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

        writeJar(classNodes, destination)
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
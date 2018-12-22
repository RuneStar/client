package org.runestar.client.updater.deob.common

import org.kxtra.slf4j.info
import org.kxtra.slf4j.getLogger
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.PUTSTATIC
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import java.lang.reflect.Modifier
import java.nio.file.Path

/**
 * Resolves and replaces the owner of field instructions by JVMS 5.4.3.2
 */
object FieldResolver : Transformer {

    private val logger = getLogger()

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)

        val resolver = Resolver(classNodes)

        var changedCount = 0

        classNodes.forEach { cn ->
            cn.methods.forEach { mn ->
                mn.instructions.iterator().forEach { insn ->
                    if (insn is FieldInsnNode) {
                        val op = insn.opcode
                        val oldOwner = insn.owner
                        insn.owner = resolver.getOwner(
                                insn.owner, insn.name, insn.desc,
                                op == GETSTATIC || op == PUTSTATIC
                        )
                        val newOwner = insn.owner
                        if (oldOwner != newOwner) changedCount++
                    }
                }
            }
        }

        logger.info { "Field instruction owners narrowed: $changedCount" }

        writeJar(classNodes, destination)
    }

    private class Resolver(classNodes: Collection<ClassNode>) {

        private val classNodesByNames = classNodes.associateBy { it.name }

        fun getOwner(owner: String, name: String, desc: String, isStatic: Boolean): String {
            var cn = classNodesByNames[owner] ?: return owner
            while (true) {
                if (cn.hasDeclaredField(name, desc, isStatic)) {
                    return cn.name
                }
                val superName = cn.superName
                cn = classNodesByNames[superName] ?: return superName
            }
        }

        private fun ClassNode.hasDeclaredField(name: String, desc: String, isStatic: Boolean): Boolean {
            return fields.any {
                it.name == name && it.desc == desc && Modifier.isStatic(it.access) == isStatic
            }
        }
    }
}
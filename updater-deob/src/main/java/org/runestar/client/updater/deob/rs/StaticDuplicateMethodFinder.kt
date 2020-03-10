package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.collect.Multimap
import com.google.common.collect.TreeMultimap
import org.kxtra.slf4j.getLogger
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.LineNumberNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.runestar.client.updater.deob.Transformer
import java.lang.reflect.Modifier
import java.nio.file.Path

object StaticDuplicateMethodFinder : Transformer.Tree() {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        val map: Multimap<String, String> = TreeMultimap.create()
        klasses.forEach { c ->
            c.methods.filter { Modifier.isStatic(it.access) && it.name != "<clinit>" }.forEach { m ->
                map.put(m.id(), c.name + "." + m.name + m.desc)
            }
        }

        map.asMap().entries.removeIf { it.value.size == 1 }

        mapper.writeValue(dir.resolve("static-methods-dup.json").toFile(), map.asMap().values.sortedBy { it.first() })
    }

    private fun MethodNode.id(): String {
        return "${Type.getReturnType(desc)}." + (instructions.lineNumberRange() ?: "*") + "." + instructions.hash()
    }

    private fun InsnList.lineNumberRange(): IntRange? {
        val lns = iterator().asSequence().mapNotNull { it as? LineNumberNode }.mapNotNull { it.line }.toList()
        if (lns.isEmpty()) return null
        return lns.first()..lns.last()
    }

    // todo
    private fun InsnList.hash(): Int {
        return iterator().asSequence().mapNotNull {
            when (it) {
                is FieldInsnNode -> it.owner + "." + it.name + ":" + it.opcode
                is MethodInsnNode ->  it.opcode.toString() + ":" + it.owner + "." + it.name
                is InsnNode -> it.opcode.toString()
                else -> null
            }
        }.toSet().hashCode()
    }
}
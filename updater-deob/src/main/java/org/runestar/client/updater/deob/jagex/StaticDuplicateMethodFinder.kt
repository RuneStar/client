package org.runestar.client.updater.deob.jagex

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.collect.Multimap
import com.google.common.collect.TreeMultimap
import org.runestar.client.updater.deob.Deobfuscator
import org.runestar.client.updater.deob.readJar
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*
import java.lang.reflect.Modifier
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

object StaticDuplicateMethodFinder : Deobfuscator {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source)

        val map: Multimap<String, String> = TreeMultimap.create()
        classNodes.forEach { c ->
            c.methods.filter { Modifier.isStatic(it.access) && it.name != "<clinit>" }.forEach { m ->
                map.put(m.id(), c.name + "." + m.name + m.desc)
            }
        }

        map.asMap().entries.removeIf { it.value.size == 1 }

        val dupFile = destination.resolveSibling(destination.fileName.toString() + ".static-methods-dup.json").toFile()
        mapper.writeValue(dupFile, map.asMap().values.sortedBy { it.first() })

        if (source != destination) {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
        }
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
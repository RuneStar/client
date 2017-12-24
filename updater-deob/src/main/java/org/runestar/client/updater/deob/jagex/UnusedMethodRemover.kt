package org.runestar.client.updater.deob.jagex

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import org.runestar.client.updater.deob.Deobfuscator
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.deob.writeJar
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import java.nio.file.Path
import java.util.*

object UnusedMethodRemover : Deobfuscator {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source)
        val classNodeNames = classNodes.associate { it.name to it }

        val supers = MultimapBuilder.hashKeys().arrayListValues().build<ClassNode, ClassNode>()
        classNodes.forEach { c ->
            c.interfaces.filter { classNodeNames.containsKey(it) }.forEach { i ->
                supers.put(c, classNodeNames.getValue(i))
            }
            if (classNodeNames.containsKey(c.superName)) {
                supers.put(c, classNodeNames.getValue(c.superName))
            }
        }

        val subs = MultimapBuilder.hashKeys().arrayListValues().build<ClassNode, ClassNode>()
        supers.forEach { k, v ->
            subs.put(v, k)
        }

        val usedMethods = classNodes.flatMap { it.methods.asIterable() }
                .flatMap { it.instructions.toArray().asIterable() }
                .mapNotNull { it as? MethodInsnNode }
                .map { it.owner + "." + it.name + it.desc }
                .toSet()

        val removedMethods = ArrayList<Pair<ClassNode, MethodNode>>()
        classNodes.forEach { c ->
            val methods = c.methods.iterator()
            while (methods.hasNext()) {
                val m = methods.next()
                m.instructions
                val mName = c.name + "." + m.name + m.desc
                if (m.name.length > 2) continue // overrides a jdk method
                if (usedMethods.contains(mName)) continue
                if (methodUsedIn(c, m, usedMethods, supers)) continue
                if (methodUsedIn(c, m, usedMethods, subs)) continue
                methods.remove()
                removedMethods.add(c to m)
            }
        }

        logger.info { "Methods removed: ${removedMethods.size}" }
        writeJar(classNodes, destination)
    }

    private fun methodUsedIn(orig: ClassNode, m: MethodNode, usedMethods: Set<String>, group: Multimap<ClassNode, ClassNode>): Boolean {
        var curr = group[orig]
        while (curr.isNotEmpty()) {
            curr.forEach { c ->
                if (usedMethods.contains(c.name + "." + m.name + m.desc)) {
                    return true
                }
            }
            curr = curr.flatMap { group[it] }
        }
        return false
    }
}
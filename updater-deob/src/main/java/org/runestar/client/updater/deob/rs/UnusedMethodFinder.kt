package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.runestar.client.updater.deob.Transformer
import java.nio.file.Path
import java.util.ArrayList
import java.util.TreeSet

object UnusedMethodFinder : Transformer.Tree() {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        val classNodeNames = klasses.associateBy { it.name }

        val supers = MultimapBuilder.hashKeys().arrayListValues().build<ClassNode, String>()
        klasses.forEach { c ->
            c.interfaces.forEach { i ->
                supers.put(c, i)
            }
            supers.put(c, c.superName)
        }

        val subs = MultimapBuilder.hashKeys().arrayListValues().build<ClassNode, String>()
        supers.forEach { k, v ->
            if (classNodeNames.containsKey(v)) {
                subs.put(classNodeNames.getValue(v), k.name)
            }
        }

        val usedMethods = klasses.asSequence().flatMap { it.methods.asSequence() }
                .flatMap { it.instructions.iterator().asSequence() }
                .mapNotNull { it as? MethodInsnNode }
                .map { it.owner + "." + it.name + it.desc }
                .toSet()

        val removedMethods = TreeSet<String>()
        klasses.forEach { c ->
            for (m in c.methods) {
                if (isMethodUsed(c, m, usedMethods, supers, subs, classNodeNames)) continue
                val mName = c.name + "." + m.name + m.desc
                removedMethods.add(mName)
            }
        }

        logger.info { "Unused methods found: ${removedMethods.size}" }

        mapper.writeValue(dir.resolve("unused-methods.json").toFile(), removedMethods)
    }

    private fun isMethodUsed(
            orig: ClassNode,
            m: MethodNode,
            usedMethods: Set<String>,
            supers: Multimap<ClassNode, String>,
            subs: Multimap<ClassNode, String>,
            classNodeNames: Map<String, ClassNode>
    ): Boolean {
        if (m.name == "<init>" || m.name == "<clinit>") return true
        val mName = orig.name + "." + m.name + m.desc
        if (usedMethods.contains(mName)) return true
        var currSupers: Collection<String> = supers[orig]
        while (currSupers.isNotEmpty()) {
            currSupers.forEach { c ->
                if (isJdkMethod(c, m.name, m.desc)) return true
                if (usedMethods.contains(c + "." + m.name + m.desc)) return true
            }
            currSupers = currSupers.filter { classNodeNames.containsKey(it) }.flatMap { supers[classNodeNames.getValue(it)] }
        }
        var currSubs = subs[orig]
        while (currSubs.isNotEmpty()) {
            currSubs.forEach { c ->
                if (usedMethods.contains(c + "." + m.name + m.desc)) return true
            }
            currSubs = currSubs.flatMap { subs[classNodeNames.getValue(it)] }
        }
        return false
    }

    private fun isJdkMethod(internalClassName: String, methodName: String, methodDesc: String): Boolean {
        try {
            var classes = listOf(Class.forName(Type.getObjectType(internalClassName).className))
            while (classes.isNotEmpty()) {
                for (c in classes) {
                    if (c.declaredMethods.any { it.name == methodName && Type.getMethodDescriptor(it) == methodDesc }) return true
                }
                classes = classes.flatMap {
                    ArrayList<Class<*>>().apply {
                        addAll(it.interfaces)
                        if (it.superclass != null) add(it.superclass)
                    }
                }
            }
        } catch (e: Exception) {

        }
        return false
    }
}
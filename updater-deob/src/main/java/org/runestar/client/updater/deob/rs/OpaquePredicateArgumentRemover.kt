package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.collect.MultimapBuilder
import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.isIntValue
import java.lang.reflect.Modifier
import java.nio.file.Path
import java.util.HashSet
import java.util.TreeMap

object OpaquePredicateArgumentRemover : Transformer.Tree() {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        val classNames = klasses.associateBy { it.name }

        val methodDescriptorsChanged = TreeMap<String, String>()
        var changedInstructionCount = 0

        val topMethods = HashSet<String>()
        for (c in klasses) {
            val supers = supers(c, classNames)
            for (m in c.methods) {
                if (supers.none { it.methods.any { it.name == m.name && it.desc == m.desc } }) {
                    topMethods.add("${c.name}.${m.name}${m.desc}")
                }
            }
        }

        val implementationsMulti = MultimapBuilder.hashKeys().arrayListValues().build<String, ClassMethod>()
        val implementations = implementationsMulti.asMap()
        for (c in klasses) {
            for (m in c.methods) {
                val s = overrides(c.name, m.name + m.desc, topMethods, classNames) ?: continue
                implementationsMulti.put(s, ClassMethod(c, m))
            }
        }

        val itr = implementations.iterator()
        for (e in itr) {
            if (e.value.any { !hasUnusedLastParamInt(it.m) }) {
                itr.remove()
            }
        }

        for (c in klasses) {
            for (m in c.methods) {
                val insnList = m.instructions
                for (insn in insnList) {
                    if (insn !is MethodInsnNode) continue
                    val s = overrides(insn.owner, insn.name + insn.desc, implementations.keys, classNames) ?: continue
                    if (!insn.previous.isIntValue) {
                        implementations.remove(s)
                    }
                }
            }
        }

        implementationsMulti.values().forEach { (c, m) ->
            val oldDesc = m.desc
            val newDesc = dropLastArg(oldDesc)
            m.desc = newDesc
            methodDescriptorsChanged["${c.name}.${m.name}$newDesc:${m.access}"] = oldDesc
        }

        for (c in klasses) {
            for (m in c.methods) {
                val insnList = m.instructions
                for (insn in insnList) {
                    if (insn !is MethodInsnNode) continue
                    if (overrides(insn.owner, insn.name + insn.desc, implementations.keys, classNames) != null) {
                        insn.desc = dropLastArg(insn.desc)
                        val prev = insn.previous
                        check(prev.isIntValue)
                        insnList.remove(prev)
                        changedInstructionCount++
                    }
                }
            }
        }

        logger.info { "Opaque predicate descriptors changed: methods ${methodDescriptorsChanged.size}, instructions: $changedInstructionCount" }

        mapper.writeValue(dir.resolve("op-descs.json").toFile(), methodDescriptorsChanged)
    }

    private fun overrides(owner: String, nameDesc: String, methods: Set<String>, classNames: Map<String, ClassNode>): String? {
        val s = "$owner.$nameDesc"
        if (s in methods) return s
        if (nameDesc.startsWith("<init>")) return null
        val classNode = classNames[owner] ?: return null
        for (sup in supers(classNode, classNames)) {
            return overrides(sup.name, nameDesc, methods, classNames) ?: continue
        }
        return null
    }

    private fun supers(c: ClassNode, classNames: Map<String, ClassNode>): Collection<ClassNode> {
        return c.interfaces.plus(c.superName).mapNotNull { classNames[it] }.flatMap { supers(it, classNames).plus(it) }
    }

    private fun hasUnusedLastParamInt(m: MethodNode): Boolean {
        val argTypes = Type.getArgumentTypes(m.desc)
        if (argTypes.isEmpty()) return false
        val lastArg = argTypes.last()
        if (lastArg != Type.BYTE_TYPE && lastArg != Type.SHORT_TYPE && lastArg != Type.INT_TYPE) return false
        if (Modifier.isAbstract(m.access)) return true
        val lastParamLocalIndex = (if (Modifier.isStatic(m.access)) -1 else 0) + (Type.getArgumentsAndReturnSizes(m.desc) shr 2) - 1
        for (insn in m.instructions) {
            if (insn !is VarInsnNode) continue
            if (insn.`var` == lastParamLocalIndex) return false
        }
        return true
    }

    private fun dropLastArg(desc: String): String {
        val type = Type.getMethodType(desc)
        return Type.getMethodDescriptor(type.returnType, *type.argumentTypes.copyOf(type.argumentTypes.size - 1))
    }

    private data class ClassMethod(val c: ClassNode, val m: MethodNode)
}
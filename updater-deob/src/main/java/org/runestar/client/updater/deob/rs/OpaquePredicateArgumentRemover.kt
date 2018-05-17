package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import org.objectweb.asm.Type
import org.objectweb.asm.tree.MethodInsnNode
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.deob.writeJar
import java.nio.file.Path

object OpaquePredicateArgumentRemover : Transformer {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        val opFile = source.resolveSibling(source.fileName.toString() + ".op.json").toFile()
        val ops = mapper.readValue<Map<String, Int>>(opFile)
        val opMethods = ops.keys

        val classNodeNames = classNodes.associate { it.name to it }
        val supers = MultimapBuilder.hashKeys().arrayListValues().build<String, String>()
        classNodes.forEach { c ->
            c.interfaces.forEach { i ->
                val v = classNodeNames[i]
                if (v != null) {
                    supers.put(c.name, v.name)
                }
            }
            val v = classNodeNames[c.superName]
            if (v != null) {
                supers.put(c.name, v.name)
            }
        }

        val subs = MultimapBuilder.hashKeys().arrayListValues().build<String, String>()
        supers.forEach { k, v ->
            subs.put(v, k)
        }

        classNodes.forEach { c ->
            c.methods.forEach { m ->
                if (methodMatches(c.name, m.name, m.desc, opMethods, supers, subs)) {
                    m.desc = fixDescriptor(m.desc)
                }
            }
        }

        classNodes.forEach { c ->
            c.methods.forEach { m ->
                val insns = m.instructions.iterator()
                while (insns.hasNext()) {
                    val insn = insns.next()
                    if (insn is MethodInsnNode) {
                        if (methodMatches(insn.owner, insn.name, insn.desc, opMethods, supers, subs)) {
                            m.instructions.remove(insn.previous)
                            insn.desc = fixDescriptor(insn.desc)
                        }
                    }
                }
            }
        }
        writeJar(classNodes, destination)
    }

    private fun methodMatches(
            orig: String,
            mName: String,
            mDesc: String,
            ms: Set<String>,
            supers: Multimap<String, String>,
            subs: Multimap<String, String>
    ): Boolean {
        var curr = listOf(orig)
        while (curr.isNotEmpty()) {
            curr.forEach { c ->
                var curr2 = listOf(c)
                while (curr2.isNotEmpty()) {
                    curr2.forEach { c2 ->
                        if (ms.contains(c2 + "." + mName + mDesc)) {
                            return true
                        }
                    }
                    curr2 = curr2.flatMap { subs[it] }
                }
                if (ms.contains(c + "." + mName + mDesc)) {
                    return true
                }
            }
            curr = curr.flatMap { supers[it] }
        }
        return false
    }

    private fun fixDescriptor(desc: String): String {
        val type = Type.getMethodType(desc)
        return Type.getMethodDescriptor(type.returnType, *type.argumentTypes.dropLast(1).toTypedArray())
    }
}
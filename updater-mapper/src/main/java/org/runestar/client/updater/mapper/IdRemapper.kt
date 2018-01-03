package org.runestar.client.updater.mapper

import com.google.common.collect.MultimapBuilder
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.tree.ClassNode

class IdRemapper(
        idClasses: Collection<IdClass>,
        classNodes: Collection<ClassNode>
) : Remapper() {

    private val supers = MultimapBuilder.hashKeys().arrayListValues().build<String, String>()

    private val names = HashMap<String, String>()

    init {
        classNodes.forEach { c ->
            supers.put(c.name, c.superName)
            c.interfaces.forEach { i ->
                supers.put(c.name, i)
            }
        }
        idClasses.forEach { c ->
            names[c.name] = c.`class`
            c.fields.forEach { f ->
                names["${f.owner}.${f.name}"] = f.field
            }
            c.methods.forEach { m ->
                names["${m.owner}.${m.name}${m.descriptor}"] = m.method
            }
        }
    }

    private fun findMember(klass: String, member: String): String? {
        var classes = listOf(klass)
        while (classes.isNotEmpty()) {
            classes.forEach { clas ->
                names["$clas.$member"]?.let {
                    return it
                }
            }
            classes = classes.flatMap { supers.get(it) }
        }
        return null
    }

    override fun map(typeName: String): String {
        return names[typeName] ?: (typeName + '$')
    }

    override fun mapFieldName(owner: String, name: String, desc: String): String {
        return findMember(owner, name) ?: (name + '_')
    }

    override fun mapMethodName(owner: String, name: String, desc: String): String {
        return findMember(owner, name + desc) ?: (name + '_')
    }
}
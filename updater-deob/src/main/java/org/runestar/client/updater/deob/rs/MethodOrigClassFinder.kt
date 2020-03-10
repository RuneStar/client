package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.objectweb.asm.tree.ClassNode
import org.runestar.client.updater.deob.Transformer
import java.nio.file.Path
import java.util.SortedSet
import java.util.TreeMap

object MethodOrigClassFinder : Transformer.Tree() {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        val dupMethods = mapper.readValue<List<SortedSet<String>>>(dir.resolve("static-methods-dup.json").toFile())
        val map = TreeMap<String, String>()

        klasses.forEach { c ->
            for (m in c.methods) {
                val name = c.name + "." + m.name + m.desc
                val set = dupMethods.firstOrNull { it.contains(name) } ?: continue
                val classNames = set.minus(name).map { it.split(".").first() }.distinct()
                check(classNames.size == 1) { "name: $name, set: $set" }
                val realClassName = classNames.first()
                if (c.name == realClassName) continue
                map[name] = realClassName
            }
        }

        mapper.writeValue(dir.resolve("methods-orig-class.json").toFile(), map)

        logger.info { "Static method original classes found: ${map.size}" }
    }
}
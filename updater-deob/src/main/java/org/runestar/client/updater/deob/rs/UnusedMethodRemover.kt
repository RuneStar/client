package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.objectweb.asm.tree.ClassNode
import org.runestar.client.updater.deob.Transformer
import java.nio.file.Path

object UnusedMethodRemover : Transformer.Tree() {

    private val mapper = jacksonObjectMapper()

    private val logger = getLogger()

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        val unusedMethodNames = mapper.readValue<Set<String>>(dir.resolve("unused-methods.json").toFile())

        klasses.forEach { c ->
            val ms = c.methods.iterator()
            while (ms.hasNext()) {
                val m = ms.next()
                val mName = c.name + "." + m.name + m.desc
                if (mName !in unusedMethodNames) continue
                ms.remove()
            }
        }

        logger.info { "Unused methods removed: ${unusedMethodNames.size}" }
    }
}
package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.deob.writeJar
import java.nio.file.Files
import java.nio.file.Path

object UnusedMethodRemover : Transformer {

    private val mapper = jacksonObjectMapper()

    private val logger = getLogger()

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)

        val unusedMethodsFile = source.resolveSibling(source.fileName.toString() + ".unused-methods.json")
        check(Files.exists(unusedMethodsFile))

        val unusedMethodNames = mapper.readValue<Set<String>>(unusedMethodsFile.toFile())

        classNodes.forEach { c ->
            val ms = c.methods.iterator()
            while (ms.hasNext()) {
                val m = ms.next()
                val mName = c.name + "." + m.name + m.desc
                if (mName !in unusedMethodNames) continue
                ms.remove()
            }
        }

        logger.info { "Unused methods removed: ${unusedMethodNames.size}" }

        writeJar(classNodes, destination)
    }
}
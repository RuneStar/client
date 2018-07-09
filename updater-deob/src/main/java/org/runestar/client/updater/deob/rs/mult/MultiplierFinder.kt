package org.runestar.client.updater.deob.rs.mult

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.tree.analysis.Analyzer
import org.runestar.client.updater.common.invert
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import java.nio.file.Path
import java.util.*

object MultiplierFinder : Transformer {

    private val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val logger = getLogger()

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)

        val decoders = MultimapBuilder.hashKeys().arrayListValues().build<String, Number>()
        val dependentDecoders = MultimapBuilder.hashKeys().arrayListValues().build<String, Pair<String, Number>>()
        val dependentEncoders = MultimapBuilder.hashKeys().arrayListValues().build<String, Pair<String, Number>>()

        val analyzer = Analyzer(MultInterpreter(decoders, dependentDecoders, dependentEncoders))

        for (c in classNodes) {
            for (m in c.methods) {
                analyzer.analyze(c.name, m)
            }
        }

        val decodersFinal = unfoldToDecoders(decoders, dependentDecoders, dependentEncoders)

        val multFile = destination.resolveSibling(destination.fileName.toString() + ".mult.json").toFile()
        mapper.writeValue(multFile, decodersFinal)

        logger.info { "Multipliers found: ${decodersFinal.size}" }

        if (source != destination) {
            writeJar(classNodes, destination)
        }
    }

    private fun unfoldToDecoders(
            decoders: Multimap<String, Number>,
            dependentDecoders: Multimap<String, Pair<String, Number>>,
            dependentEncoders: Multimap<String, Pair<String, Number>>
    ): Map<String, Number> {

        val decodersFinal = TreeMap<String, Number>()

        decoders.asMap().mapValuesTo(decodersFinal) { e ->
            checkNotNull(e.value.maxBy { n -> Collections.frequency(e.value, n) })
        }

        var startSize: Int
        do {
            startSize = decodersFinal.size

            dependentDecoders.entries().forEach { (f, p) ->
                if (f !in decodersFinal) {
                    val otherF = p.first
                    val value = p.second
                    val otherDecoder = decodersFinal[otherF] ?: return@forEach
                    val nUnfolded: Number = when (value) {
                        is Int -> value.toInt() / invert(otherDecoder).toInt()
                        is Long -> value.toLong() / invert(otherDecoder).toLong()
                        else -> error(value)
                    }
                    if (isMultiplier(nUnfolded)) {
                        decodersFinal[f] = nUnfolded
                    }
                }
            }

            dependentEncoders.entries().forEach { (f, p) ->
                if (f !in decodersFinal) {
                    val otherF = p.first
                    val value = p.second
                    val otherDecoder = decodersFinal[otherF] ?: return@forEach
                    val nUnfolded: Number = when (value) {
                        is Int -> value.toInt() / otherDecoder.toInt()
                        is Long -> value.toLong() / otherDecoder.toLong()
                        else -> error(value)
                    }
                    if (isMultiplier(nUnfolded)) {
                        decodersFinal[f] = invert(nUnfolded)
                    }
                }
            }

        } while (startSize != decodersFinal.size)

        return decodersFinal
    }
}
package org.runestar.client.api

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema
import org.runestar.client.plugins.spi.FileReadWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

object PropertiesFileReadWriter : FileReadWriter {

    private val FACTORY = JavaPropsFactory()

    private val MAPPER = JavaPropsMapper(FACTORY)
            .findAndRegisterModules()
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            as JavaPropsMapper

    private val SCHEMA = JavaPropsSchema()
            .withLineEnding(System.lineSeparator())

    override val fileExtension = "properties"

    override fun <T> read(file: Path, type: Class<T>): T {
        Files.newBufferedReader(file, StandardCharsets.UTF_8).use { reader ->
            val parser = FACTORY.createParser(reader)
            parser.schema = SCHEMA
            return MAPPER.readValue(parser, type)
        }
    }

    override fun write(file: Path, value: Any) {
        Files.newBufferedWriter(file, StandardCharsets.UTF_8).use { writer ->
            val generator = FACTORY.createGenerator(writer)
            generator.schema = SCHEMA
            MAPPER.writeValue(generator, value)
        }
    }
}
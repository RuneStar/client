package org.runestar.client.api

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.runestar.client.plugins.spi.FileReadWriter
import java.nio.file.Path

internal object YamlFileReadWriter : FileReadWriter {

    private val mapper = YAMLMapper().findAndRegisterModules()

    override val fileExtension = "yml"

    override fun <T> read(file: Path, type: Class<T>): T {
        return mapper.readValue(file.toFile(), type)
    }

    override fun write(file: Path, value: Any) {
        return mapper.writeValue(file.toFile(), value)
    }
}
package com.runesuite.client

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.runesuite.client.plugins.FileReadWriter
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
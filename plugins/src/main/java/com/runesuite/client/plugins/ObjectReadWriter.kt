package com.runesuite.client.plugins

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import java.io.IOException
import java.nio.file.Path

interface ObjectReadWriter<T> {

    @Throws(IOException::class)
    fun read(file: Path, type: Class<T>): T

    @Throws(IOException::class)
    fun write(file: Path, value: T)

    class Yaml<T> : ObjectReadWriter<T> {

        private companion object {
            val YAML = YAMLFactory()
        }

        private val mapper = YAMLMapper(YAML).findAndRegisterModules()

        override fun read(file: Path, type: Class<T>): T {
            return mapper.readValue(file.toFile(), type)
        }

        override fun write(file: Path, value: T) {
            mapper.writeValue(file.toFile(), value)
        }
    }
}
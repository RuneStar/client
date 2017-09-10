package com.runesuite.client.plugins.framework

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import java.io.IOException
import java.nio.file.Path

interface ObjectReadWriter<T> {

    @Throws(IOException::class)
    fun read(file: Path, type: Class<T>): T

    @Throws(IOException::class)
    fun write(file: Path, type: Class<T>, value: T)

    class Yaml<T> : ObjectReadWriter<T> {

        private val mapper = ObjectMapper(YAMLFactory()).findAndRegisterModules()

        override fun read(file: Path, type: Class<T>): T {
            return mapper.readValue(file.toFile(), type)
        }

        override fun write(file: Path, type: Class<T>, value: T) {
            mapper.writeValue(file.toFile(), value)
        }
    }
}
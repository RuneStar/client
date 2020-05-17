package org.runestar.client.api.plugins

import java.io.IOException
import java.nio.file.Path

/**
 * Uses a consistent encoding to read and write arbitrary objects to and from files
 */
interface FileReadWriter {

    /**
     * The supported file extension such as `"json"` or `"xml"`
     */
    val fileExtension: String

    @Throws(IOException::class)
    fun <T> read(file: Path, type: Class<T>): T

    @Throws(IOException::class)
    fun write(file: Path, value: Any)
}
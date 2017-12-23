package org.runestar.client.plugins

import java.nio.file.Path
import java.util.jar.JarFile

internal fun verifyJar(jar: Path): Boolean {
    return try {
        JarFile(jar.toFile(), true).close()
        true
    } catch (e: Exception) {
        false
    }
}

internal fun jarFileBytes(jar: Path): Map<String, ByteArray> {
    val files = HashMap<String, ByteArray>()
    JarFile(jar.toFile()).use { jarFile ->
        jarFile.stream().filter { !it.isDirectory }.forEach { entry ->
            jarFile.getInputStream(entry).use { input ->
                files[entry.name] = input.readBytes()
            }
        }
    }
    return files
}
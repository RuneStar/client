package com.runesuite.client.plugins

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
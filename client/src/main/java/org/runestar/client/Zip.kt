package org.runestar.client

import java.net.URLClassLoader
import java.nio.file.Path
import java.util.jar.JarFile

internal fun URLClassLoader(path: Path): URLClassLoader {
    return URLClassLoader(arrayOf(path.toUri().toURL()))
}

internal fun verifyJar(jar: Path): Boolean {
    return try {
        JarFile(jar.toFile(), true).close()
        true
    } catch (e: Exception) {
        false
    }
}
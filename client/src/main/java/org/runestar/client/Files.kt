package org.runestar.client

import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
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

internal fun downloadFile(url: URL, destination: Path) {
    url.openStream().use { input ->
        Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING)
    }
}
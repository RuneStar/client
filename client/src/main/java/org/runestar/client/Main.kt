@file:JvmName("Main")

package org.runestar.client

import org.runestar.client.api.Application
import org.runestar.client.inject.inject
import org.runestar.general.downloadGamepack
import org.runestar.general.updateRevision
import java.net.URLClassLoader
import java.nio.file.Path
import java.nio.file.Paths
import java.util.jar.JarFile

fun main(args: Array<String>) {
    Application.start(injectGamepack())
}

private val TMPDIR = Paths.get(System.getProperty("java.io.tmpdir"))

private fun injectGamepack(): ClassLoader {
    val revision = updateRevision()
    val injectedGamepackPath = TMPDIR.resolve("runescape-gamepack.$revision.inject.jar")
    if (!verifyJar(injectedGamepackPath)) {
        val gamepackPath = TMPDIR.resolve("runescape-gamepack.$revision.jar")
        if (!verifyJar(gamepackPath)) {
            downloadGamepack(gamepackPath)
        }
        inject(gamepackPath, injectedGamepackPath)
    }
    return URLClassLoader(injectedGamepackPath)
}

private fun URLClassLoader(path: Path): URLClassLoader {
    return URLClassLoader(arrayOf(path.toUri().toURL()))
}

private fun verifyJar(jar: Path): Boolean {
    try {
        JarFile(jar.toFile(), true).close()
        return true
    } catch (e: Exception) {
        return false
    }
}
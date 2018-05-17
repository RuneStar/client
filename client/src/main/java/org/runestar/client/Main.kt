@file:JvmName("Main")

package org.runestar.client

import org.runestar.client.api.Application
import org.runestar.client.inject.inject
import org.runestar.client.updater.HOOKS
import org.runestar.general.downloadGamepack
import org.runestar.general.updateRevision
import java.net.URLClassLoader
import java.nio.file.Path
import java.nio.file.Paths
import java.util.jar.JarFile

fun main(args: Array<String>) {
    Application.start(injectGamepack())
}

private fun injectGamepack(): ClassLoader {
    val tmpdir = Paths.get(System.getProperty("java.io.tmpdir"))
    val revision = updateRevision()
    val injectedGamepackPath = tmpdir.resolve("$revision-${clientVersion()}.zip")
    if (!verifyJar(injectedGamepackPath)) {
        val gamepackPath = tmpdir.resolve("runescape-gamepack.$revision.jar")
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

private fun clientVersion(): String {
    return HOOKS.hashCode().toString()
}
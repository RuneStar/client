@file:JvmName("Main")

package org.runestar.client

import com.google.common.io.Files
import org.runestar.client.api.Application
import org.runestar.client.inject.inject
import org.runestar.general.downloadGamepack
import org.runestar.general.updateRevision
import java.io.IOException
import java.net.URLClassLoader
import java.nio.file.Paths
import java.util.jar.JarFile

fun main(args: Array<String>) {
    Application.start(injectGamepack())
}

private fun injectGamepack(): ClassLoader {
    val revision = updateRevision()
    val gamepackPath = Paths.get(System.getProperty("java.io.tmpdir"), "runescape-gamepack.$revision.jar")
    try {
        JarFile(gamepackPath.toFile(), true).close()
    } catch (e: IOException) {
        // jar does not exist or was partially downloaded
        downloadGamepack(gamepackPath)
    }
    val injectedGamepackDirectory = Files.createTempDir().toPath()
    inject(gamepackPath, injectedGamepackDirectory)
    return URLClassLoader(arrayOf(injectedGamepackDirectory.toUri().toURL()))
}

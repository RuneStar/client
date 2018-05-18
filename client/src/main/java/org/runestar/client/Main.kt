@file:JvmName("Main")

package org.runestar.client

import com.google.common.hash.Hashing
import org.runestar.client.api.Application
import org.runestar.client.injector.inject
import org.runestar.client.updater.hooksFile
import org.runestar.general.downloadGamepack
import org.runestar.general.updateRevision
import java.nio.file.Paths

fun main(args: Array<String>) {
    Application.start(injectGamepack())
}

private fun injectGamepack(): ClassLoader {
    val tmpdir = Paths.get(System.getProperty("java.io.tmpdir"))
    val revision = updateRevision() // todo
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

private fun clientVersion(): String {
    return Hashing.crc32().hashBytes(hooksFile.readBytes()).asInt().toString() // todo
}
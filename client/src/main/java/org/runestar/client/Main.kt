@file:JvmName("Main")

package org.runestar.client

import org.runestar.client.api.Application
import org.runestar.client.injector.inject
import org.runestar.client.updater.HOOKS
import org.runestar.general.downloadGamepack
import org.runestar.general.updateRevision
import java.nio.file.Paths

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

private fun clientVersion(): String {
    return HOOKS.hashCode().toString() // todo
}
package com.runesuite.client.dev.plugins

import java.io.IOException
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchKey
import java.nio.file.WatchService
import java.util.jar.JarFile

fun Path.registerAll(watchService: WatchService): WatchKey {
    return register(watchService,
            StandardWatchEventKinds.ENTRY_MODIFY,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE
    )
}

fun verifyJar(jar: Path): Boolean {
    try {
        JarFile(jar.toFile(), true)
        return true
    } catch (e: IOException) {
        return false
    } catch (e: SecurityException) {
        return false
    }
}
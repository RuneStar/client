package com.runesuite.client.plugins.framework

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
    return try {
        JarFile(jar.toFile(), true)
        true
    } catch (e: IOException) {
        false
    } catch (e: SecurityException) {
        false
    }
}
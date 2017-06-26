package com.runesuite.client.dev.plugins

import mu.KotlinLogging
import java.io.Closeable
import java.lang.reflect.Modifier
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

class PluginLoader(val pluginsDirectory: Path, val settingsDirectory: Path) : Closeable {

    private companion object {
        val BUFFER_TIME_MS = 50L
    }

    private val logger = KotlinLogging.logger {  }

    private val plugins = HashMap<Path, Collection<Plugin<*>>>()

    private val settings = HashMap<String, Plugin<*>>()

    private val jarWatchService: WatchService = FileSystems.getDefault().newWatchService().apply {
        pluginsDirectory.registerAll(this)
    }

    private val settingsWatchService: WatchService = FileSystems.getDefault().newWatchService().apply {
        settingsDirectory.registerAll(this)
    }

    private val jarThread = Thread(fileWatcher(jarWatchService, BUFFER_TIME_MS) {
        val path = pluginsDirectory.resolve(it.context() ?: return@fileWatcher)
        if (path.toFile().extension == "jar") {
            plugins.remove(path)?.forEach {
                settings.remove(it.javaClass.name)
                it.destroy()
            }
            if (verifyJar(path)) {
                loadJar(path)
            }
        }
    }, pluginsDirectory.toString())

    private val settingsThread = Thread(fileWatcher(settingsWatchService, BUFFER_TIME_MS)  {
        val path = settingsDirectory.resolve(it.context() ?: return@fileWatcher)
        val plugin = settings[path.toFile().nameWithoutExtension] ?: return@fileWatcher
        plugin.settingsFileChanged(it.kind())
    }, settingsDirectory.toString())

    init {
        Files.walkFileTree(pluginsDirectory, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (attrs.isRegularFile && file.toFile().extension == "jar" && verifyJar(file)) {
                    loadJar(file)
                }
                return super.visitFile(file, attrs)
            }
        })
        jarThread.start()
        settingsThread.start()
    }

    private fun loadJar(jar: Path) {
        logger.debug { "Loading plugin jar: $jar" }
        val jarClassLoader = newJarClassLoader(jar)
        @Suppress("UNCHECKED_CAST")
        val jarPlugins = jarClassLoader.urlClasses.filter { Plugin::class.java.isAssignableFrom(it) }
                .map { it as Class<out Plugin<*>> }
                .filter { !Modifier.isAbstract(it.modifiers) }
                .map { it.newInstance() }
        jarPlugins.forEach {
            it.loader = this
            it.create()
            settings[it.javaClass.name] = it
        }
        plugins[jar] = jarPlugins
    }

    override fun close() {
        jarThread.interrupt()
        settingsThread.interrupt()
        plugins.values.flatMap { it }.forEach { it.destroy() }
        plugins.clear()
        settings.clear()
    }

    private inline fun fileWatcher(watchService: WatchService, bufferTimeMs: Long, crossinline onEvent: (WatchEvent<Path>) -> Unit): Runnable {
        return Runnable {
            while (true) {
                val key: WatchKey
                try {
                    key = watchService.take() // blocks
                    Thread.sleep(bufferTimeMs) // accumulate duplicate events
                } catch (e: InterruptedException) {
                    logger.warn { "Stopping. Interrupted." }
                    return@Runnable
                }
                @Suppress("UNCHECKED_CAST")
                for (event in key.pollEvents() as Collection<WatchEvent<Path>>) {
                    logger.debug { "WatchEvent(kind=${event.kind()}, context=${event.context()}, count=${event.count()})" }
                    onEvent(event)
                }
                if (!key.reset()) {
                    logger.error { "Stopping. Error." }
                    return@Runnable
                }
            }
        }
    }
}
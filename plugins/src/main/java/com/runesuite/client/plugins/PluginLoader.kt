package com.runesuite.client.plugins

import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.kxtra.slf4j.loggerfactory.getLogger
import java.io.Closeable
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class PluginLoader(
        private val pluginsJarsDir: Path,
        private val pluginsDir: Path
) : Closeable {

    private val logger = getLogger()

    private val currentJarPluginNames = HashMap<Path, Collection<String>>()

    private val currentPlugins = HashMap<String, PluginHolder<*>>()

    private val executor = Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("plugins%d").build())

    private val watchService = FileSystems.getDefault().newWatchService()

    init {
        Files.walkFileTree(pluginsJarsDir, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (attrs.isRegularFile && file.toFile().extension == "jar" && verifyJar(file)) {
                    executor.submit { loadJar(file) }
                }
                return super.visitFile(file, attrs)
            }
        })

        pluginsJarsDir.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE)

        Thread({
            while (true) {
                val key: WatchKey
                try {
                    key = watchService.take() // blocks
                    Thread.sleep(100L) // accumulates duplicate events
                } catch (e: InterruptedException) {
                    logger.error("WatchService interrupted early.", e)
                    return@Thread
                } catch (e: ClosedWatchServiceException) {
                    return@Thread
                }
                val dir = key.watchable() as Path
                key.pollEvents().forEach { weRaw ->
                    @Suppress("UNCHECKED_CAST")
                    val we = weRaw as WatchEvent<Path>
                    val ctx = we.context() ?: return@forEach
//                    executor.submit {
//                        logger.debug { "$dir.$ctx:${we.kind()}" }
//                    }
                    if (dir == pluginsJarsDir && ctx.toFile().extension == "jar") {
                        val jarPath = pluginsJarsDir.resolve(we.context())
                        executor.submit {
                            val pluginNames = currentJarPluginNames.remove(jarPath)
                            if (pluginNames != null) {
                                pluginNames.forEach { className ->
                                    checkNotNull(currentPlugins.remove(className)).destroy()
                                }
                            }
                            if (Files.exists(jarPath) && verifyJar(jarPath)) {
                                loadJar(jarPath)
                            }
                        }
                    } else if (dir != pluginsDir && ctx.fileName.toString() == PluginHolder.SETTINGS_FILE_NAME) {
                        executor.submit {
                            currentPlugins[dir.fileName.toString()]?.settingsFileChanged()
                        }
                    }
                }
                key.reset()
            }
        }).start()
    }

    private fun loadJar(jar: Path) {
        val plugins = PluginClassLoader.load(jar)
        plugins.forEach { plugin ->
            val pluginDir = pluginsDir.resolve(plugin.javaClass.name)
            Files.createDirectories(pluginDir)
            val watchKey = pluginDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE)
            val pluginHolder = PluginHolder(plugin, watchKey)
            pluginHolder.create()
            currentPlugins[plugin.javaClass.name] = pluginHolder
        }
        currentJarPluginNames[jar] = plugins.map { it.javaClass.name }
    }

    override fun close() {
        watchService.close()
        executor.submit {
            currentPlugins.values.forEach { it.destroy() }
        }
        executor.shutdown()
        executor.awaitTermination(10L, TimeUnit.SECONDS)
    }
}
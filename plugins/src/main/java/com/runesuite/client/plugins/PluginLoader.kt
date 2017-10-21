package com.runesuite.client.plugins

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.reactivex.schedulers.Schedulers
import mu.KotlinLogging
import java.io.Closeable
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class PluginLoader(
        val pluginsDirectory: Path,
        val settingsDirectory: Path
) : Closeable {

    private val logger = KotlinLogging.logger {  }

    private val pluginsWatchService = FileSystems.getDefault().newWatchService()

    private val settingsWatchService = FileSystems.getDefault().newWatchService()

    private val currentJarPluginNames = HashMap<Path, Collection<String>>()

    private val currentPlugins = HashMap<String, PluginHolder<*>>()

    private val executor = Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("plugins%d").build())

    private val scheduler = Schedulers.from(executor)

    init {
        newDirectoryWatchObservable(pluginsDirectory, pluginsWatchService)
                .observeOn(scheduler)
                .filter { it.context().toFile().extension == "jar" }
                .subscribe { we ->
                    logger.debug { "${we.kind()} > ${we.context()}" }
                    val jarPath = pluginsDirectory.resolve(we.context())
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

        newDirectoryWatchObservable(settingsDirectory, settingsWatchService)
                .observeOn(scheduler)
                .filter { it.kind() != StandardWatchEventKinds.ENTRY_CREATE }
                .subscribe { we ->
                    logger.debug { "${we.kind()} > ${we.context()}" }
                    val path = pluginsDirectory.resolve(we.context())
                    val plugin = currentPlugins[path.toFile().nameWithoutExtension] ?: return@subscribe
                    plugin.settingsFileChanged()
                }
    }

    private fun loadJar(jar: Path) {
        val plugins = PluginClassLoader.load(jar)
        plugins.forEach { plugin ->
            val pluginHolder = PluginHolder(this, plugin)
            pluginHolder.create()
            currentPlugins[plugin.javaClass.name] = pluginHolder
        }
        currentJarPluginNames[jar] = plugins.map { it.javaClass.name }
    }

    override fun close() {
        pluginsWatchService.close()
        settingsWatchService.close()
        executor.submit {
            logger.debug("close")
            currentPlugins.values.forEach { it.destroy() }
        }
        executor.shutdown()
        executor.awaitTermination(10L, TimeUnit.SECONDS)
    }
}
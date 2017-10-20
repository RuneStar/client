package com.runesuite.client.plugins

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import mu.KotlinLogging
import java.io.Closeable
import java.lang.reflect.Modifier
import java.net.URLClassLoader
import java.nio.file.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class PluginLoader(
        val pluginsDirectory: Path,
        val settingsDirectory: Path
) : Closeable {

    private val logger = KotlinLogging.logger {  }

    private val pluginsWatchService = FileSystems.getDefault().newWatchService()

    private val settingsWatchService = FileSystems.getDefault().newWatchService()

    private val jars = HashMap<Path, Collection<PluginHolder<*>>>()

    private val plugins = ConcurrentHashMap<String, PluginHolder<*>>()

    val executor = Executors.newSingleThreadExecutor()

    private val scheduler = Schedulers.from(executor)

    init {
        newDirectoryWatchObservable(pluginsDirectory, pluginsWatchService)
                .observeOn(scheduler)
                .filter { it.context().toFile().extension == "jar" }
                .subscribe { we ->
                    val path = pluginsDirectory.resolve(we.context())
                    jars.remove(path)?.forEach { p ->
                        plugins.remove(p.javaClass.name)
                        p.end()
                    }
                    if (verifyJar(path)) {
                        loadJar(path)
                    }
                }

        newDirectoryWatchObservable(settingsDirectory, settingsWatchService)
                .observeOn(scheduler)
                .filter { it.context() != null && it.kind() != StandardWatchEventKinds.ENTRY_CREATE }
                .subscribe { we ->
                    val path = pluginsDirectory.resolve(we.context())
                    val plugin = plugins[path.toFile().nameWithoutExtension] ?: return@subscribe
                    plugin.settingsFileChanged()
                }
    }

    private fun loadJar(jar: Path) {
        val holders = URLClassLoader(jar).plugins().map { PluginHolder(this, it) }
        holders.forEach {
            it.begin()
            plugins[it.plugin.javaClass.name] = it
        }
        jars[jar] = holders
    }

    private fun URLClassLoader.plugins(): List<Plugin<*>> {
        return urlClasses
                .filter { !Modifier.isAbstract(it.modifiers) }
                .filter { Plugin::class.java.isAssignableFrom(it) }
                .mapNotNull {
                    try {
                        it.getDeclaredConstructor().newInstance() as Plugin<*>
                    } catch (e: Throwable) {
                        null
                    }
                }
    }

    override fun close() {
        pluginsWatchService.close()
        settingsWatchService.close()
        executor.submit {
            logger.debug("close")
            plugins.values.forEach { it.end() }
        }
        executor.shutdown()
        executor.awaitTermination(10L, TimeUnit.SECONDS)
    }
}
package com.runesuite.client.dev.plugins

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import mu.KotlinLogging
import java.io.Closeable
import java.lang.reflect.Modifier
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.ConcurrentHashMap

class PluginLoader(val pluginsDirectory: Path, val settingsDirectory: Path) : Closeable {

    private val pluginsWatchService = FileSystems.getDefault().newWatchService()

    private val settingsWatchService = FileSystems.getDefault().newWatchService()

    private val logger = KotlinLogging.logger {  }

    private val plugins = HashMap<Path, Collection<Plugin<*>>>()

    private val settings = ConcurrentHashMap<String, Plugin<*>>()

    init {
        Files.walkFileTree(pluginsDirectory, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (attrs.isRegularFile && file.toFile().extension == "jar" && verifyJar(file)) {
                    loadJar(file)
                }
                return super.visitFile(file, attrs)
            }
        })

        newDirectoryWatchObservable(pluginsDirectory, pluginsWatchService)
                .filter { it.context() != null }
                .filter { it.context().toFile().extension == "jar" }
                .subscribe { we ->
                    val path = pluginsDirectory.resolve(we.context())
                    plugins.remove(path)?.forEach { p ->
                        settings.remove(p.javaClass.name)
                        p.destroy()
                    }
                    if (verifyJar(path)) {
                        loadJar(path)
                    }
                }

        newDirectoryWatchObservable(settingsDirectory, settingsWatchService)
                .filter { it.context() != null }
                .subscribe { we ->
                    val path = pluginsDirectory.resolve(we.context())
                    val plugin = settings[path.toFile().nameWithoutExtension] ?: return@subscribe
                    plugin.settingsFileChanged(we.kind())
                }
    }

    private fun loadJar(jar: Path) {
        logger.debug { "Loading plugin jar: $jar" }
        val jarClassLoader = newJarClassLoader(jar)
        @Suppress("UNCHECKED_CAST")
        val jarPlugins = jarClassLoader.urlClasses
                .filter { Plugin::class.java.isAssignableFrom(it) }
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
        plugins.values.flatMap { it }.forEach { it.destroy() }
        plugins.clear()
        settings.clear()
        pluginsWatchService.close()
        settingsWatchService.close()
    }

    fun newDirectoryWatchObservable(directory: Path, watchService: WatchService): Observable<WatchEvent<Path>> {
        return Observable.create<WatchEvent<Path>> { emitter ->
            directory.registerAll(watchService)
            while (true) {
                val key: WatchKey
                try {
                    key = watchService.take() // blocks
                    Thread.sleep(50L) // accumulates duplicate events
                } catch (e: ClosedWatchServiceException) {
                    emitter.onComplete()
                    return@create
                } catch (e: InterruptedException) {
                    emitter.onError(e)
                    return@create
                }
                @Suppress("UNCHECKED_CAST")
                for (event in key.pollEvents() as Collection<WatchEvent<Path>>) {
                    logger.debug { "$directory: onNext. Event(${event.kind()}, ${event.context()}, ${event.count()})" }
                    emitter.onNext(event)
                }
                if (!key.reset()) {
                    emitter.onComplete()
                    return@create
                }
            }
        }.subscribeOn(Schedulers.newThread())
    }
}
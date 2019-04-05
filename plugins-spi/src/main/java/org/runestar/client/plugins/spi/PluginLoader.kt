package org.runestar.client.plugins.spi

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.kxtra.slf4j.debug
import org.kxtra.slf4j.getLogger
import java.io.Closeable
import java.io.IOException
import java.nio.file.ClosedWatchServiceException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchKey
import java.util.ServiceLoader
import java.util.SortedMap
import java.util.TreeMap
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @param lifeCycleExecutor Used to execute all methods in [Plugin].
 */
class PluginLoader(
        private val pluginsDir: Path,
        private val settingsReadWriter: FileReadWriter,
        private val lifeCycleExecutor: Executor
) : Closeable {

    private val logger = getLogger()

    private val pluginsMap: SortedMap<String, Holder<*>>

    val plugins: Collection<Holder<*>> get() = pluginsMap.values

    private val loaderThread: ExecutorService = Executors.newSingleThreadExecutor()

    private val watchService = FileSystems.getDefault().newWatchService()

    private val settingsFileName = "settings.${settingsReadWriter.fileExtension}"

    init {
        pluginsMap = findPlugins().associateTo(TreeMap()) { it.name to Holder(it) }
        logger.debug { "Plugins found: ${pluginsMap.keys}" }

        loaderThread.submit {
            plugins.forEach { it.init() }
            Thread {
                while (true) {
                    val key: WatchKey
                    try {
                        key = watchService.take() // blocks
                        Thread.sleep(150L) // accumulates duplicate events
                    } catch (e: ClosedWatchServiceException) {
                        return@Thread
                    }
                    val dir = key.watchable() as Path
                    for (event in key.pollEvents()) {
                        val file = event.context() as Path? ?: continue
                        if (file.fileName.toString() == settingsFileName) {
                            loaderThread.submit {
                                pluginsMap[dir.fileName.toString()]?.settingsFileChanged()
                            }
                            break
                        }
                    }
                    key.reset()
                }
            }.start()
        }
    }

    private fun findPlugins(): Collection<Plugin<*>> {
        return ServiceLoader.load(Plugin::class.java, javaClass.classLoader).toList()
    }

    override fun close() {
        logger.debug("Closing...")
        watchService.close()
        loaderThread.submit {
            plugins.forEach { it.destroy() }
        }
        loaderThread.shutdown()
        loaderThread.awaitTermination(5L, TimeUnit.SECONDS)
        logger.debug("Closed")
    }

    inner class Holder<T : PluginSettings>(private val plugin: Plugin<T>) {

        private val logger = getLogger("Holder($name)")

        private var ignoreNextEvent = false

        private val watchKey: WatchKey

        private val directory: Path = pluginsDir.resolve(name)

        private val settingsFile: Path = directory.resolve("settings.${settingsReadWriter.fileExtension}")

        private lateinit var settings: T

        val name: String get() = plugin.name

        val ctx = PluginContext(directory, settingsFile)

        init {
            Files.createDirectories(directory)
            watchKey = directory.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE
            )
        }

        internal fun init() {
            createSettings()
            initPlugin()
            if (settings.enabled) {
                startPlugin()
            }
        }

        private fun writeSettings() {
            try {
                ignoreNextEvent = true
                logger.debug("Writing settings...")
                settingsReadWriter.write(settingsFile, settings)
                logger.debug("Write successful.")
            } catch (e: IOException) {
                logger.warn("Write failed.", e)
                if (isRunning) {
                    stopPlugin()
                    settings.enabled = false
                }
            }
        }

        private fun readSettings() {
            try {
                settings = settingsReadWriter.read(settingsFile, plugin.defaultSettings.javaClass)
                logger.debug("Read successful.")
            } catch (e: IOException) {
                val settingsFileCopy = directory.resolve("settings-${System.currentTimeMillis()}.${settingsReadWriter.fileExtension}")
                settings = plugin.defaultSettings
                Files.move(settingsFile, settingsFileCopy, StandardCopyOption.REPLACE_EXISTING)
                logger.warn("Read failed. Moving old settings to ${settingsFileCopy.fileName}. Reverting to default settings.", e)
                writeSettings()
            }
        }

        private fun createSettings() {
            if (Files.exists(settingsFile)) {
                logger.debug("Settings file exists. Reading...")
                readSettings()
            } else {
                logger.debug("Settings file does not exist. Using default settings.")
                settings = plugin.defaultSettings
                writeSettings()
            }
        }

        internal fun settingsFileChanged() {
            if (ignoreNextEvent) {
                // ignore events caused by this class writing
                ignoreNextEvent = false
                return
            }
            stopPlugin()
            if (Files.notExists(settingsFile)) {
                logger.debug("Settings file missing. Switching to default settings.")
                settings = plugin.defaultSettings
                writeSettings()
            } else {
                logger.debug("Settings file modified. Reading new settings...")
                readSettings()
            }
            if (settings.enabled) startPlugin()
        }

        internal fun destroy() {
            watchKey.cancel()
            stopPlugin()
            _isRunningChanged.onComplete()
        }

        private fun initPlugin() {
            logger.debug("Requesting initialization")
            lifeCycleExecutor.execute {
                logger.debug("Initializing...")
                try {
                    plugin.init(ctx)
                    logger.debug("Initialized")
                } catch (t: Throwable) {
                    logger.warn("Failed to initialize", t)
                }
            }
        }

        private fun startPlugin() {
            if (isRunning) return
            logger.debug("Requesting start")
            isRunning = true
            _isRunningChanged.onNext(true)
            lifeCycleExecutor.execute {
                settings.write = {
                    loaderThread.submit {
                        synchronized(settings) {
                            writeSettings()
                        }
                    }
                }
                logger.debug("Starting...")
                try {
                    plugin.start(settings)
                    logger.debug("Started")
                } catch (t: Throwable) {
                    logger.warn("Failed to start", t)
                }
            }
        }

        private fun stopPlugin() {
            if (!isRunning) return
            logger.debug("Requesting stop")
            isRunning = false
            _isRunningChanged.onNext(false)
            lifeCycleExecutor.execute {
                logger.debug("Stopping...")
                try {
                    plugin.stop()
                    logger.debug("Stopped")
                } catch (t: Throwable) {
                    logger.warn("Failed to stop", t)
                }
            }
        }

        private var isRunning = false

        fun setIsRunning(value: Boolean) {
            loaderThread.submit {
                if (value == isRunning) return@submit
                settings.enabled = value
                if (value) {
                    startPlugin()
                    writeSettings()
                } else {
                    writeSettings()
                    stopPlugin()
                }
            }
        }

        private val _isRunningChanged = BehaviorSubject.createDefault(false)

        val isRunningChanged: Observable<Boolean> = _isRunningChanged
    }
}
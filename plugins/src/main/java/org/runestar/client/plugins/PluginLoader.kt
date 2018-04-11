package org.runestar.client.plugins

import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.kxtra.slf4j.loggerfactory.getLogger
import java.io.Closeable
import java.nio.file.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit

class PluginLoader(
        classLoader: ClassLoader,
        private val pluginsDir: Path,
        private val settingsReadWriter: FileReadWriter
) : Closeable {

    private companion object {
        val threadFactory: ThreadFactory = ThreadFactoryBuilder().setNameFormat("plugins%d").build()
    }

    private val logger = getLogger()

    private val pluginNames: Map<String, PluginHolder<*>>

    val plugins: Collection<PluginContext<*>>

    private val executor: ExecutorService = Executors.newSingleThreadExecutor(threadFactory)

    private val watchService = FileSystems.getDefault().newWatchService()

    private val settingsFileName = "settings.${settingsReadWriter.fileExtension}"

    init {
        val ps = findPlugins(classLoader)
        val holders = ps.map { PluginHolder.of(it, pluginsDir, settingsReadWriter, watchService) }
        plugins = holders.map { it.ctx }
        pluginNames = holders.associateBy { it.ctx.name }

        executor.submit {
            holders.forEach { it.init() }
            Thread({
                while (true) {
                    val key: WatchKey
                    try {
                        key = watchService.take() // blocks
                        Thread.sleep(150L) // accumulates duplicate events
                    } catch (e: ClosedWatchServiceException) {
                        return@Thread
                    }
                    val dir = key.watchable() as Path
                    key.pollEvents().forEach { weRaw ->
                        @Suppress("UNCHECKED_CAST")
                        val we = weRaw as WatchEvent<Path>
                        val ctx = we.context() ?: return@forEach
                        if (ctx.fileName.toString() == settingsFileName) {
                            executor.submit {
                                pluginNames[dir.fileName.toString()]?.settingsFileChanged()
                            }
                        }
                    }
                    key.reset()
                }
            }).start()
        }
    }

    private fun findPlugins(classLoader: ClassLoader): Collection<Plugin<*>> {
        val sl = ServiceLoader.load(Plugin::class.java, classLoader)
        val ps = sl.toList()
        logger.debug("Plugins found: ${ps.joinToString { it.name }}")
        return ps
    }

    override fun close() {
        logger.debug("Closing...")
        watchService.close()
        executor.submit {
            pluginNames.values.forEach { it.destroy() }
        }
        executor.shutdown()
        executor.awaitTermination(5L, TimeUnit.SECONDS)
        logger.debug("Closed")
    }

    fun start(plugin: PluginContext<*>) {
        val h = pluginNames[plugin.name] ?: return
        executor.submit {
            if (h.ctx.isRunning()) return@submit
            h.ctx.settings.enabled = true
            h.startPlugin()
            h.writeSettings()
        }
    }

    fun stop(plugin: PluginContext<*>) {
        val h = pluginNames[plugin.name] ?: return
        executor.submit {
            if (!h.ctx.isRunning()) return@submit
            h.ctx.settings.enabled = false
            h.writeSettings()
            h.stopPlugin()
        }
    }
}
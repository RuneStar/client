package org.runestar.client.plugins.spi

import org.kxtra.slf4j.loggerfactory.getLogger
import java.io.IOException
import java.nio.file.*

internal class PluginHolder<T : PluginSettings>(
        val plugin: Plugin<T>,
        val ctx: PluginContext<T>,
        val settingsReadWriter: FileReadWriter,
        val watchKey: WatchKey
) {

    private val logger = getLogger("PluginHolder(${ctx.name})")

    private var ignoreNextEvent = false

    fun init() {
        createSettings()
        plugin.init(ctx)
        if (ctx.settings.enabled) {
            startPlugin()
        }
    }

    internal fun writeSettings() {
        try {
            ignoreNextEvent = true
            logger.debug("Writing settings...")
            settingsReadWriter.write(ctx.settingsFile, ctx.settings)
            logger.debug("Write successful.")
        } catch (e: IOException) {
            logger.warn("Write failed.", e)
            if (ctx.isRunning()) {
                stopPlugin()
                ctx.settings.enabled = false
            }
        }
    }

    private fun readSettings() {
        try {
            ctx.settings = settingsReadWriter.read(ctx.settingsFile, plugin.defaultSettings.javaClass)
            logger.debug("Read successful.")
        } catch (e: IOException) {
            logger.warn("Read failed. Reverting to default settings.", e)
            ctx.settings = plugin.defaultSettings
            writeSettings()
        }
    }

    private fun createSettings() {
        if (Files.exists(ctx.settingsFile)) {
            logger.debug("Settings file exists. Reading...")
            readSettings()
        } else {
            logger.debug("Settings file does not exist. Using default settings.")
            ctx.settings = plugin.defaultSettings
            writeSettings()
        }
    }

    internal fun settingsFileChanged() {
        if (ignoreNextEvent) {
            // ignore events caused by this class writing
            ignoreNextEvent = false
            return
        }
        if (ctx.isRunning()) stopPlugin()
        if (Files.notExists(ctx.settingsFile)) {
            logger.debug("Settings file missing. Switching to default settings.")
            ctx.settings = plugin.defaultSettings
            writeSettings()
        } else {
            logger.debug("Settings file modified. Reading new settings...")
            readSettings()
        }
        if (ctx.isRunning()) {
            startPlugin()
        }
    }

    internal fun destroy() {
        watchKey.cancel()
        if (ctx.isRunning()) {
            stopPlugin()
        }
    }

    internal fun startPlugin() {
        try {
            logger.debug("Starting...")
            plugin.start()
            logger.debug("Started")
        } catch (e: Exception) {
            logger.warn("Exception starting plugin.", e)
            stopPlugin()
            ctx.settings.enabled = false
        }
    }

    internal fun stopPlugin() {
        try {
            logger.debug("Stopping...")
            plugin.stop()
            logger.debug("Stopped")
        } catch (e: Exception) {
            logger.warn("Exception stopping plugin.", e)
            ctx.settings.enabled = false
        }
    }

    companion object {

        fun <T : PluginSettings> of(
                plugin: Plugin<T>,
                pluginsDir: Path,
                settingsReadWriter: FileReadWriter,
                watchService: WatchService
        ): PluginHolder<T> {
            val pluginDir = pluginsDir.resolve(plugin.name)
            Files.createDirectories(pluginDir)
            val watchKey = pluginDir.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE
            )
            val ctx = PluginContext.of(pluginsDir, plugin, settingsReadWriter)
            return PluginHolder(plugin, ctx, settingsReadWriter, watchKey)
        }
    }
}
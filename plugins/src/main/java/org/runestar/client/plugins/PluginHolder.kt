package org.runestar.client.plugins

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.WatchKey
import java.util.concurrent.ExecutorService

internal class PluginHolder<T : PluginSettings>(
        private val plugin: Plugin<T>,
        private val watchKey: WatchKey,
        private val executor: ExecutorService,
        private val settingsReadWriter: FileReadWriter
) : PluginHandle {

    override val directory = watchKey.watchable() as Path

    private val logger get() = plugin.logger

    override val settingsFile: Path =
            directory.resolve("${PluginHandle.SETTINGS_FILE_NAME_BASE}.${settingsReadWriter.fileExtension}")

    private var ignoreNextEvent = false

    init {
        plugin.directory = directory
        createSettings()
        if (isRunning) {
            startPlugin()
        }
    }

    override fun start() {
        executor.submit {
            if (isRunning) return@submit
            plugin.settings.enabled = true
            startPlugin()
            writeSettings()
        }
    }

    override fun stop() {
        executor.submit {
            if (!isRunning) return@submit
            plugin.settings.enabled = false
            writeSettings()
            stopPlugin()
        }
    }

    override val isRunning: Boolean get() {
        return plugin.settings.enabled
    }

    private fun writeSettings() {
        try {
            ignoreNextEvent = true
            logger.info("Writing settings...")
            settingsReadWriter.write(settingsFile, plugin.settings)
            logger.info("Write successful.")
        } catch (e: IOException) {
            logger.warn("Write failed.", e)
            if (isRunning) {
                stopPlugin()
                plugin.settings.enabled = false
            }
        }
    }

    private fun readSettings() {
        try {
            plugin.settings = settingsReadWriter.read(settingsFile, plugin.defaultSettings.javaClass)
            logger.info("Read successful.")
        } catch (e: IOException) {
            logger.warn("Read failed. Reverting to default settings.", e)
            plugin.settings = plugin.defaultSettings
            writeSettings()
        }
    }

    private fun createSettings() {
        if (Files.exists(settingsFile)) {
            logger.info("Settings file exists. Reading...")
            readSettings()
        } else {
            logger.info("Settings file does not exist. Using default settings.")
            plugin.settings = plugin.defaultSettings
            writeSettings()
        }
    }

    internal fun settingsFileChanged() {
        if (ignoreNextEvent) {
            // ignore events caused by this class writing
            ignoreNextEvent = false
            return
        }
        if (isRunning) stopPlugin()
        if (Files.notExists(settingsFile)) {
            logger.info("Settings file missing. Switching to default settings.")
            plugin.settings = plugin.defaultSettings
            writeSettings()
        } else {
            logger.info("Settings file modified. Reading new settings...")
            readSettings()
        }
        if (isRunning) {
            startPlugin()
        }
    }

    internal fun destroy() {
        watchKey.cancel()
        if (isRunning) {
            stopPlugin()
        }
    }

    private fun startPlugin() {
        try {
            plugin.start()
        } catch (e: Exception) {
            logger.warn("Exception starting plugin.", e)
            stopPlugin()
            plugin.settings.enabled = false
        }
    }

    private fun stopPlugin() {
        try {
            plugin.stop()
        } catch (e: Exception) {
            logger.warn("Exception stopping plugin.", e)
            plugin.settings.enabled = false
        }
    }

    override fun toString(): String {
        return plugin.name
    }
}
package com.runesuite.client.plugins

import mu.KotlinLogging
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

internal class PluginHolder<T : PluginSettings>(
        loader: PluginLoader,
        val plugin: Plugin<T>
) {

    companion object {
        const val SETTINGS_FILE_EXTENSION = ".txt"
        private val settingsField = Plugin::class.java.getDeclaredField(Plugin<*>::settings.name).apply {
            isAccessible = true
        }
    }

    private val logger = KotlinLogging.logger("${javaClass.simpleName}(${plugin.javaClass.simpleName})")

    val settingsFile = loader.settingsDirectory.resolve(plugin.javaClass.name + SETTINGS_FILE_EXTENSION)

    private var ignoreNextEvent = false

    private var active = false

    private fun tryWrite(file: Path, value: T) {
        try {
            ignoreNextEvent = true
            logger.debug("Writing settings...")
            plugin.settingsWriter.write(file, value)
            logger.debug("Write successful.")
        } catch (e: IOException) {
            logger.error(e) { "Write failed." }
            safeStopPlugin()
        }
    }

    fun begin() {
        logger.debug { "Begin." }
        if (Files.exists(settingsFile)) {
            logger.debug("Settings file exists. Reading...")
            try {
                val readSettings = plugin.settingsWriter.read(settingsFile, plugin.defaultSettings.javaClass)
                settingsField.set(plugin, readSettings)
                logger.debug("Read successful.")
            } catch (e: IOException) {
                logger.warn(e) { "Read failed. Reverting to default settings." }
                settingsField.set(plugin, plugin.defaultSettings)
                tryWrite(settingsFile, plugin.settings)
            }
        } else {
            logger.debug("Settings file does not exist. Using default settings.")
            settingsField.set(plugin, plugin.defaultSettings)
            tryWrite(settingsFile, plugin.settings)
        }
        safeStartPlugin()
    }

    fun settingsFileChanged() {
        if (ignoreNextEvent) {
            // ignore events caused by this class writing
            ignoreNextEvent = false
            return
        }
        safeStopPlugin()
        if (Files.notExists(settingsFile)) {
            logger.debug("Settings file missing. Switching to default settings.")
            settingsField.set(plugin, plugin.defaultSettings)
            tryWrite(settingsFile, plugin.defaultSettings)
        } else {
            logger.debug("Settings file modified. Reading new settings...")
            try {
                val readSettings = plugin.settingsWriter.read(settingsFile, plugin.defaultSettings.javaClass)
                logger.debug("Read successful.")
                settingsField.set(plugin, readSettings)
            } catch (e: IOException) {
                logger.warn(e) { "Read failed." }
                tryWrite(settingsFile, plugin.settings)
            }
        }
        safeStartPlugin()
    }

    fun end() {
        logger.debug("End.")
        safeStopPlugin()
    }

    private fun safeStartPlugin() {
        if (active || !plugin.settings.active) return
        try {
            plugin.start()
            active = true
        } catch (e: Throwable) {
            logger.error(e) { "Exception starting plugin." }
        }
    }

    private fun safeStopPlugin() {
        if (!active || !plugin.settings.active) return
        try {
            active = false
            plugin.stop()
        } catch (e: Throwable) {
            logger.error(e) { "Exception stopping plugin." }
        }
    }
}
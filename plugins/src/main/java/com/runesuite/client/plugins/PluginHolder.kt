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
        private val field = Plugin::class.java.getDeclaredField(Plugin<*>::settings.name).apply {
            isAccessible = true
        }
    }

    private val logger = KotlinLogging.logger("${javaClass.simpleName}(${plugin.javaClass.simpleName})")

    val settingsFile = loader.settingsDirectory.resolve(plugin.javaClass.name + SETTINGS_FILE_EXTENSION)

    @Volatile
    private var ignoreNextEvent = false

    private fun tryWrite(file: Path, type: Class<T>, value: T) {
        try {
            ignoreNextEvent = true
            logger.debug("Writing settings...")
            plugin.settingsWriter.write(file, type, value)
            logger.debug("Write successful")
        } catch (e: IOException) {
            logger.error(e) { "Write failed" }
            plugin.stop()
        }
    }

    fun begin() {
        logger.debug { "Begin." }
        if (Files.exists(settingsFile)) {
            logger.debug("Settings file exists. Reading...")
            try {
                val readSettings = plugin.settingsWriter.read(settingsFile, plugin.defaultSettings.javaClass)
                field.set(plugin, readSettings)
                logger.debug("Read successful")
            } catch (e: IOException) {
                logger.warn(e) { "Read failed. Reverting to default settings." }
                field.set(plugin, plugin.defaultSettings)
                tryWrite(settingsFile, plugin.defaultSettings.javaClass, plugin.settings)
            }
        } else {
            logger.debug("Settings file does not exist. Using default settings.")
            field.set(plugin, plugin.defaultSettings)
            tryWrite(settingsFile, plugin.defaultSettings.javaClass, plugin.settings)
        }
        if (plugin.settings.active) {
            plugin.start()
        }
    }

    fun settingsFileChanged() {
        if (ignoreNextEvent) {
            // ignore events caused by this class writing
            ignoreNextEvent = false
            return
        }
        if (Files.notExists(settingsFile)) {
            logger.debug("Settings file missing.")
            if (plugin.settings.active) {
                plugin.stop()
            }
            logger.debug("Switching to default settings.")
            field.set(plugin, plugin.defaultSettings)
            tryWrite(settingsFile, plugin.defaultSettings.javaClass, plugin.defaultSettings)
            if (plugin.settings.active) {
                plugin.start()
            }
        } else {
            logger.debug("Settings file modified.")
            if (plugin.settings.active) {
                plugin.stop()
            }
            logger.debug("Reading new settings...")
            try {
                val readSettings = plugin.settingsWriter.read(settingsFile, plugin.defaultSettings.javaClass)
                logger.debug("Read successful.")
                field.set(plugin, readSettings)
            } catch (e: IOException) {
                logger.warn(e) { "Read failed." }
                tryWrite(settingsFile, plugin.defaultSettings.javaClass, plugin.settings)
            }
            if (plugin.settings.active) {
                plugin.start()
            }
        }
    }

    fun end() {
        logger.debug("End.")
        if (plugin.settings.active) {
            plugin.stop()
        }
    }
}
package org.runestar.client.plugins.spi

import org.kxtra.slf4j.loggerfactory.getLogger
import org.slf4j.Logger
import java.nio.file.Path

class PluginContext<T : PluginSettings>(
        val defaultSettings: T,
        val name: String,
        val logger: Logger,
        val directory: Path,
        val settingsFile: Path
) {

    lateinit var settings: T
        internal set

    override fun toString(): String {
        return name
    }

    fun isRunning(): Boolean {
        return this::settings.isInitialized && settings.enabled
    }

    companion object {

        fun<T : PluginSettings> of(
                pluginsDir: Path,
                plugin: Plugin<T>,
                settingsReadWriter: FileReadWriter
        ): PluginContext<T> {
            val name = plugin.name
            val dir = pluginsDir.resolve(name)
            return PluginContext(
                    plugin.defaultSettings,
                    name,
                    getLogger(name),
                    dir,
                    dir.resolve("settings.${settingsReadWriter.fileExtension}")
            )
        }
    }
}
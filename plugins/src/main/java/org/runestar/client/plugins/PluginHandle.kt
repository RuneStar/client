package org.runestar.client.plugins

import java.nio.file.Path

interface PluginHandle {

    val name: String

    fun start()

    fun stop()

    val isRunning: Boolean

    val directory: Path

    val settingsFile: Path

    val logFile: Path

    companion object {
        const val SETTINGS_FILE_NAME_BASE = "settings"
        const val LOG_FILE_NAME = "plugin.log"
    }
}
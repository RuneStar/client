package org.runestar.client.plugins

import java.nio.file.Path

interface PluginHandle {

    fun start()

    fun stop()

    val isRunning: Boolean

    val directory: Path

    val settingsFile: Path

    companion object {
        const val SETTINGS_FILE_NAME_BASE = "settings"
    }
}
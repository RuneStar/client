package com.runesuite.client.plugins

import java.nio.file.Path

interface PluginHandle {

    val name: String

    fun enable()

    fun disable()

    val isDestroyed: Boolean

    val isEnabled: Boolean

    val directory: Path

    val settingsFile: Path

    val logFile: Path
}
package org.runestar.client.plugins.spi

import java.nio.file.Path

data class PluginContext(
        val directory: Path,
        val settingsFile: Path
)
package org.runestar.client.api.plugins

import java.nio.file.Path

data class PluginContext(
        val directory: Path,
        val settingsFile: Path
)
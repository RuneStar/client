package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Canvas
import com.runesuite.client.game.api.live.Players
import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.access.XClient
import com.runesuite.client.game.raw.access.XEvictingHashTable
import com.runesuite.client.game.raw.access.XModel
import com.runesuite.client.game.raw.access.XNpcDefinition
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import java.awt.Color

class TestPlugin : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
    }
}
package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.access.XClient
import com.runesuite.client.game.raw.access.XEvictingHashTable
import com.runesuite.client.game.raw.access.XModel
import com.runesuite.client.game.raw.access.XNpcDefinition
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings

class TestPlugin : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
    }
}
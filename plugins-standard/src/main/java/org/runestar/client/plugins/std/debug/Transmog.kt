package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin

class Transmog : DisposablePlugin<Transmog.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {

        add(Game.ticks.subscribe {
            Players.local?.appearance?.let {
                it.accessor.npcTransformId = settings.npcId
            }
        })
    }

    override fun stop() {
        super.stop()

        Players.local?.appearance?.let {
            it.accessor.npcTransformId = -1
        }
    }

    class Settings(
            val npcId: Int = 403
    ) : PluginSettings()
}
package org.runestar.client.plugins.pinoverheadtext

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Players
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.plugins.spi.PluginSettings

class PinOverheadText : DisposablePlugin<PinOverheadText.Settings>() {

    override val name = "Pin Overhead Text"

    override val defaultSettings = Settings()

    override fun start() {
        add(XClient.doCycle.exit.subscribe {
            Players.forEach { player ->
                if (!settings.autoChatOnly || player.accessor.isAutoChatting) {
                    if (player.overheadTextCyclesRemaining > 0) {
                        player.overheadTextCyclesRemaining = 2
                    }
                }

            }
        })
    }

    class Settings(
            val autoChatOnly: Boolean = true
    ) : PluginSettings()
}
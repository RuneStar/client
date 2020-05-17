package org.runestar.client.plugins.pinoverheadtext

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.Players
import org.runestar.client.raw.access.XClient
import org.runestar.client.api.plugins.PluginSettings

class PinOverheadText : DisposablePlugin<PinOverheadText.Settings>() {

    override val name = "Pin Overhead Text"

    override val defaultSettings = Settings()

    override fun onStart() {
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
package org.runestar.client.plugins.dev

import org.runestar.client.api.Fonts
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class ClientPreferencesDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            val p = CLIENT.clientPreferences ?: return@subscribe
            val strings = listOf(
                    "titleMusicDisabled: ${p.titleMusicDisabled}",
                    "roofsHidden: ${p.roofsHidden}",
                    "hideUsername: ${p.hideUsername}",
                    "windowMode: ${p.windowMode}",
                    "rememberedUsername: ${p.rememberedUsername}",
                    "parameters: ${p.parameters}"
            )
            g.font = Fonts.PLAIN_12
            g.color = Color.WHITE
            val x = 20
            var y = 40
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }
        })
    }
}
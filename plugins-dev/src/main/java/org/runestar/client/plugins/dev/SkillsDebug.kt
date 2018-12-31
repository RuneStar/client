package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.Fonts
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Stats
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class SkillsDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(LiveCanvas.repaints.subscribe { g ->
            g.font = Fonts.PLAIN_12
            g.color = Color.WHITE

            var y = 35
            val x = 5
            Stats.forEach { k, v ->
                g.drawString("$k=$v", x, y)
                y += g.font.size + 5
            }
        })
    }
}
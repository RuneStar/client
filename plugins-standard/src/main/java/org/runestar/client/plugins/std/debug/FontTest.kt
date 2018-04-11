package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.general.fonts.*
import java.awt.Color

class FontTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    private val fonts = listOf(
            RUNESCAPE_CHAT_BOLD_FONT,
            RUNESCAPE_CHAT_FONT,
            RUNESCAPE_LARGE_FONT,
            RUNESCAPE_SMALL_FONT,
            RUNESCAPE_NPC_CHAT_FONT
    )

    val s = "1234567890 abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_/:><"

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.WHITE
            val x = 20
            var y = 20

            for (f in fonts) {
                g.font = f
                g.drawString(s, x, y)
                y += 30
            }
        })
    }
}
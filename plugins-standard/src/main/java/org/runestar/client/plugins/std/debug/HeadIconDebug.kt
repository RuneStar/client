package org.runestar.client.plugins.std.debug

import org.kxtra.swing.graphics2d.drawString
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.FontForm
import java.awt.Font

class HeadIconDebug : DisposablePlugin<HeadIconDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()

        add(LiveCanvas.repaints.subscribe { g ->
            g.font = settings.font.get()
            g.color = settings.color.get()

            Players.all.forEach { p ->
                val pos = p.position.copy(height = 0)
                if (!pos.isLoaded) return@forEach
                val pt = pos.toScreen() ?: return@forEach
                val string = "${p.headIconPk},${p.headIconPrayer}"
                g.drawString(string, pt)
            }
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}
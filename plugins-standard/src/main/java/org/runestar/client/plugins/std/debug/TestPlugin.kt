package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Widgets
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.plugins.utils.ColorForm
import org.runestar.client.plugins.utils.DisposablePlugin
import org.runestar.client.plugins.utils.FontForm
import java.awt.Font

class TestPlugin : DisposablePlugin<TestPlugin.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = settings.color.get()

            Widgets.flat.filter { it.accessor.itemIds != null }.forEach { w ->
                println(w)
            }
            println()
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}
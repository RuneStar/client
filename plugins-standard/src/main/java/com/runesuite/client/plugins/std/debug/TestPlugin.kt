package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Chat
import com.runesuite.client.game.api.live.LiveCanvas
import com.runesuite.client.game.api.live.Players
import com.runesuite.client.game.api.live.Widgets
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.utils.ColorForm
import com.runesuite.client.plugins.utils.DisposablePlugin
import com.runesuite.client.plugins.utils.FontForm
import java.awt.Font

class TestPlugin : DisposablePlugin<TestPlugin.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = settings.color.get()

//            Widgets.flat.filter { it.accessor.itemIds != null }.forEach { w ->
//                println(w)
//            }
//            println()
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}
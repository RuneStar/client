package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.game.api.live.Widgets
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color

class WidgetDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()

        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.WHITE
            g.font = RUNESCAPE_CHAT_FONT
            val mouse = Mouse.location
            val mouseWidget = Widgets.flat
                    .filter {
                        val s = it.shape
                        s != null && mouse in s
                    }
                    .minBy { it.width + it.height } ?: return@subscribe
            g.draw(mouseWidget.shape)
            val hierarchy = ArrayList<Widget>()
            var w: Widget? = mouseWidget
            while (w != null) {
                hierarchy.add(w)
                w = w.ancestor
            }
            val hierarchyString = hierarchy.joinToString(" > ") { it.idString() }
            g.drawString(hierarchyString, 30, 40)
        })
    }

    private fun Widget.idString(): String {
        var s = "${parentId.group}.${parentId.parent}"
        if (this is Widget.Child) {
            s += ":$childId"
        }
        return s
    }
}
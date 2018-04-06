package org.runestar.client.plugins.std.widgetexplorer

import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.game.api.live.Widgets
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.drawStringShadowed
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color
import javax.swing.SwingUtilities

class WidgetExplorer : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Widget Explorer"

    @Volatile
    private var frame: ExplorerFrame? = null

    override fun start() {
        super.start()
        SwingUtilities.invokeAndWait {
            frame = ExplorerFrame()
        }
        add(LiveCanvas.repaints.subscribe { g ->
            frame?.selectedWidget?.shape?.let {
                g.color = Color.WHITE
                g.draw(it)
            }
        })
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.MAGENTA
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
            g.color = Color.WHITE
            val hierarchyString = hierarchy.joinToString(" > ") { it.idString() }
            g.drawStringShadowed(hierarchyString, 30, 40)
        })
    }

    override fun stop() {
        super.stop()
        SwingUtilities.invokeLater {
            frame?.dispose()
        }
    }
}
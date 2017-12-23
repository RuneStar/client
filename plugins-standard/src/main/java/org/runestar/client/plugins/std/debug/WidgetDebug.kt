package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.game.api.live.Widgets
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.plugins.utils.DisposablePlugin
import java.awt.Color
import java.awt.Font

class WidgetDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    val font = Font(Font.MONOSPACED, Font.BOLD, 13)

    val parentColors = listOf(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.LIGHT_GRAY, Color.GRAY)

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            g.font = font
            val mousePt = Mouse.location

            val smallestHoveredWidget = Widgets.flat
                    .filter { it.isVisible }
                    .filter { it.shape?.contains(mousePt) ?: false }
                    .minBy { it.dimension.run { it.width + it.height } }

            smallestHoveredWidget?.let { w ->
                g.color = Color.WHITE
                w.shape?.let { g.draw(it) }
                var p = w.ancestor
                var pIndex = 0
                var parentsString = "ancestors="
                while (p != null) {
                    parentsString += "${p.group.id}:${p.parentId.parent} > "
                    if (p.isVisible) {
                        p.shape?.let {
                            g.color = parentColors[pIndex]
                            g.draw(it)
                        }
                    }
                    p = p.ancestor
                    pIndex++
                }
                val strings = listOf(
                        parentsString,
                        "$w",
                        "text: ${w.text}",
                        "type: ${w.accessor.type}",
//                        "textColor: ${w.textColor}",
//                        "spellActionName: ${w.accessor.spellActionName}",
//                        "spellName: ${w.accessor.spellName}",
//                        "pad: ${w.accessor.paddingX}, ${w.accessor.paddingY}",
//                        "okText: ${w.accessor.okText}",
//                        "item: ${w.accessor.itemId} x ${w.accessor.itemQuantity}",
//                        "scroll: ${w.accessor.scrollX}, ${w.accessor.scrollY}, ${w.accessor.scrollMax}",
                        "dataText: ${w.accessor.dataText}"
//                        "string1: ${w.accessor.string1}"
                )
                val x = 20
                var y = 47
                g.color = Color.WHITE
                strings.forEach { s ->
                    g.drawString(s, x, y)
                    y += 20
                }
            }
        })
    }
}
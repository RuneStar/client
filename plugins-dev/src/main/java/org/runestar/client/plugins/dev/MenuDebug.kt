package org.runestar.client.plugins.dev

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Menu
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color

class MenuDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = RUNESCAPE_CHAT_FONT
            g.color = Color.WHITE
            val strings = ArrayList<String>()

            strings.apply {
                add("menu")
                add("isOpen: ${Menu.isOpen}")
                add("shape: ${Menu.shape}")
                add("optionsCount: ${Menu.optionsCount}")
                add("options:")
                Menu.options.mapTo(this) { it.toString() }
            }

            strings.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }

            Menu.optionShapes.forEach { g.draw(it) }
        })

        add(Menu.openings.subscribe { pt ->
            ctx.logger.info("Menu opened at $pt")
        })

        add(Menu.actions.subscribe { a ->
            ctx.logger.info(a.toString())
        })
    }
}
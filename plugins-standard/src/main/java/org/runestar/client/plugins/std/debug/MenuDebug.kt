package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Menu
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.plugins.utils.ColorForm
import org.runestar.client.plugins.utils.DisposablePlugin
import org.runestar.client.plugins.utils.FontForm
import java.awt.Font

class MenuDebug : DisposablePlugin<MenuDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()

        add(LiveCanvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = settings.font.get()
            g.color = settings.color.get()
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
            logger.info("Menu opened at $pt")
        })

        add(Menu.actions.subscribe { a ->
            logger.info(a.toString())
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}
package org.runestar.client.plugins.groundmarkers

import org.runestar.client.api.forms.BasicStrokeForm
import org.runestar.client.api.forms.KeyCodeForm
import org.runestar.client.api.forms.RgbaForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.GlobalTile
import org.runestar.client.game.api.MenuOption
import org.runestar.client.game.api.MenuOptionOpcode
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Menu
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints

class GroundMarkers : DisposablePlugin<GroundMarkers.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Ground Markers"

    private val MARK_ACTION = "Mark"

    override fun onStart() {
        add(LiveCanvas.repaints.subscribe(::onDraw))
        add(Menu.optionAdditions.subscribe(::onMenuOptionAdded))
        add(Menu.actions.subscribe(::onMenuAction))
    }

    private fun onDraw(g: Graphics2D) {
        if (Game.state != GameState.LOGGED_IN) return
        g.color = settings.color.value
        g.stroke = settings.stroke.value
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        for (marker in settings.markers) {
            if (!marker.isLoaded()) continue
            val sceneTile = marker.toSceneTile()
            if (!Game.visibilityMap.isVisible(sceneTile)) continue
            g.draw(sceneTile.outline())
        }
    }

    private fun onMenuOptionAdded(menuOption: MenuOption) {
        if (menuOption.opcode != MenuOptionOpcode.WALK_HERE || !Keyboard.isKeyPressed(settings.keyCode.value)) return
        Menu.addOption(MenuOption.of(MenuOptionOpcode.CANCEL, 0, 0, 0, "", MARK_ACTION, false))
    }

    private fun onMenuAction(menuOption: MenuOption) {
        if (menuOption.opcode != MenuOptionOpcode.CANCEL || menuOption.action != MARK_ACTION) return
        val tile = Game.selectedTile?.toGlobalTile() ?: return
        val idx = settings.markers.indexOf(tile)
        synchronized(settings) {
            if (idx != -1) {
                settings.markers.removeAt(idx)
            } else {
                settings.markers.add(tile)
            }
            return@synchronized
        }
        settings.write()
    }

    class Settings(
            val stroke: BasicStrokeForm = BasicStrokeForm(1f),
            val color: RgbaForm = RgbaForm(Color.YELLOW),
            val keyCode: KeyCodeForm = KeyCodeForm("SHIFT"),
            val markers: ArrayList<GlobalTile> = ArrayList()
    ) : PluginSettings()
}
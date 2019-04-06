package org.runestar.client.plugins.groundmarkers

import org.runestar.client.api.forms.BasicStrokeForm
import org.runestar.client.api.forms.KeyCodeForm
import org.runestar.client.api.forms.RgbaForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.GlobalTile
import org.runestar.client.game.api.MenuOption
import org.runestar.client.game.api.MenuOptionOpcode
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveScene
import org.runestar.client.game.api.live.Menu
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints

class GroundMarkers : DisposablePlugin<GroundMarkers.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Ground Markers"

    private val MARK_ACTION = "Mark"

    private val sceneMarkers = ArrayList<SceneTile>()

    override fun onStart() {
        add(LiveCanvas.repaints.subscribe(::onDraw))
        add(Menu.optionAdditions.subscribe(::onMenuOptionAdded))
        add(Menu.actions.subscribe(::onMenuAction))
        add(LiveScene.reloads.subscribe { reloadSceneMarkers() })
        reloadSceneMarkers()
    }

    override fun onStop() {
        sceneMarkers.clear()
    }

    private fun reloadSceneMarkers() {
        sceneMarkers.clear()
        for (globalTile in settings.markers) {
            Game.fromTemplate(globalTile).filterTo(sceneMarkers) { it.isLoaded }
        }
    }

    private fun onDraw(g: Graphics2D) {
        if (Game.state != GameState.LOGGED_IN || sceneMarkers.isEmpty()) return
        g.color = settings.color.value
        g.stroke = settings.stroke.value
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        for (sceneMarker in sceneMarkers) {
            if (!Game.visibilityMap.isVisible(sceneMarker)) continue
            g.draw(sceneMarker.outline())
        }
    }

    private fun onMenuOptionAdded(menuOption: MenuOption) {
        if (menuOption.opcode != MenuOptionOpcode.WALK_HERE || !Keyboard.isKeyPressed(settings.keyCode.value)) return
        Menu.addOption(MenuOption.of(MenuOptionOpcode.CANCEL, 0, 0, 0, "", MARK_ACTION, false))
    }

    private fun onMenuAction(menuOption: MenuOption) {
        if (menuOption.opcode != MenuOptionOpcode.CANCEL || menuOption.action != MARK_ACTION) return
        val selectedTile = Game.selectedTile ?: return
        val gt = Game.toTemplate(selectedTile)
        val sts = Game.fromTemplate(gt)
        val idx = settings.markers.indexOf(gt)
        synchronized(settings) {
            if (idx != -1) {
                settings.markers.removeAt(idx)
                sceneMarkers.removeAll(sts)
            } else {
                settings.markers.add(gt)
                sceneMarkers.addAll(sts)
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
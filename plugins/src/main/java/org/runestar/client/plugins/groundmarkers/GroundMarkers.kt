package org.runestar.client.plugins.groundmarkers

import org.runestar.client.api.forms.BasicStrokeForm
import org.runestar.client.api.forms.KeyCodeForm
import org.runestar.client.api.forms.RgbaForm
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.GameState
import org.runestar.client.api.game.GlobalTile
import org.runestar.client.api.game.MiniMenuOption
import org.runestar.client.api.game.MiniMenuOpcode
import org.runestar.client.api.game.SceneTile
import org.runestar.client.api.game.live.Game
import org.runestar.client.api.game.live.Keyboard
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.game.live.Scene
import org.runestar.client.api.game.live.MiniMenu
import org.runestar.client.api.game.live.VisibilityMap
import org.runestar.client.api.plugins.PluginSettings
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints

class GroundMarkers : DisposablePlugin<GroundMarkers.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Ground Markers"

    private val MARK_ACTION = "Mark"

    private val sceneMarkers = ArrayList<SceneTile>()

    override fun onStart() {
        add(Canvas.repaints.subscribe(::onDraw))
        add(MiniMenu.optionAdditions.subscribe(::onMenuOptionAdded))
        add(MiniMenu.actions.subscribe(::onMenuAction))
        add(Scene.reloads.subscribe { reloadSceneMarkers() })
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
            if (!VisibilityMap.isVisible(sceneMarker)) continue
            g.draw(sceneMarker.outline())
        }
    }

    private fun onMenuOptionAdded(menuOption: MiniMenuOption) {
        if (menuOption.opcode != MiniMenuOpcode.WALK || !Keyboard.isKeyPressed(settings.keyCode.value)) return
        MiniMenu.addOption(MiniMenuOption.of(MiniMenuOpcode.CANCEL, 0, 0, 0, "", MARK_ACTION, false))
    }

    private fun onMenuAction(menuOption: MiniMenuOption) {
        if (menuOption.opcode != MiniMenuOpcode.CANCEL || menuOption.action != MARK_ACTION) return
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
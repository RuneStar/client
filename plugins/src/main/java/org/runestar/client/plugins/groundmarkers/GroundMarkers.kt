package org.runestar.client.plugins.groundmarkers

import org.runestar.client.api.forms.BasicStrokeForm
import org.runestar.client.api.forms.KeyCodeForm
import org.runestar.client.api.forms.RgbaForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.GlobalTile
import org.runestar.client.game.api.MenuOption
import org.runestar.client.game.api.MenuOptionOpcode
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Menu
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.RenderingHints

class GroundMarkers : DisposablePlugin<GroundMarkers.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Ground Markers"

    private val MARK_ACTION = "Mark"

    private val markers = LinkedHashSet<GlobalTile>()

    override fun onStart() {
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = settings.color.value
            g.stroke = settings.stroke.value
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            for (marker in markers) {
                if (!marker.isLoaded()) return@subscribe
                val sceneTile = marker.toSceneTile()
                if (!Game.visibilityMap.isVisible(sceneTile)) return@subscribe
                g.draw(sceneTile.outline())
            }
        })

        add(Menu.optionAdditions.subscribe { mo ->
            if (mo.opcode != MenuOptionOpcode.WALK_HERE || !Keyboard.isKeyPressed(settings.keyCode.value)) return@subscribe
            Menu.addOption(MenuOption.of(MenuOptionOpcode.CANCEL, 0, 0, 0, "", MARK_ACTION, false))
        })

        add(Menu.actions.subscribe { mo ->
            if (mo.opcode != MenuOptionOpcode.CANCEL || mo.action != MARK_ACTION) return@subscribe
            val tile = Game.selectedTile?.toGlobalTile() ?: return@subscribe
            if (!markers.add(tile)) {
                markers.remove(tile)
            }
        })
    }

    override fun onStop() {
        markers.clear()
    }

    class Settings(
            val stroke: BasicStrokeForm = BasicStrokeForm(1f),
            val color: RgbaForm = RgbaForm(Color.YELLOW),
            val keyCode: KeyCodeForm = KeyCodeForm("SHIFT")
    ) : PluginSettings()
}
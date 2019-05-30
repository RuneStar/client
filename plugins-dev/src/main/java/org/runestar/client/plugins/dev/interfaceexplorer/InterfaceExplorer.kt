package org.runestar.client.plugins.dev.interfaceexplorer

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.util.drawStringShadowed
import org.runestar.client.api.Fonts
import org.runestar.client.game.api.Component
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.game.api.live.Components
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import javax.swing.SwingUtilities

class InterfaceExplorer : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Interface Explorer"

    @Volatile
    private var frame: ExplorerFrame? = null

    override fun onStart() {
        SwingUtilities.invokeAndWait {
            frame = ExplorerFrame()
        }
        add(LiveCanvas.repaints.subscribe { g ->
            frame?.selectedComponent?.shape?.let {
                g.color = Color.WHITE
                g.draw(it)
            }
        })
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.MAGENTA
            g.font = Fonts.PLAIN_12
            val mouse = Mouse.location
            val mouseComponent = Components.flat
                    .filter {
                        val s = it.shape
                        s != null && mouse in s && it.isActive
                    }
                    .minBy { it.width + it.height } ?: return@subscribe
            g.draw(mouseComponent.shape)
            val hierarchy = ArrayList<Component>()
            var w: Component? = mouseComponent
            while (w != null) {
                hierarchy.add(w)
                w = w.parent
            }
            g.color = Color.WHITE
            val hierarchyString = hierarchy.joinToString(" > ") { it.idString() }
            g.drawStringShadowed(hierarchyString, 30, 40)
        })
    }

    override fun onStop() {
        SwingUtilities.invokeLater {
            frame?.dispose()
        }
    }
}
package org.runestar.client.plugins.dev

import org.kxtra.swing.graphics.drawString
import org.runestar.client.api.Fonts
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.game.live.Scene
import org.runestar.client.api.game.live.Mouse
import org.runestar.client.api.game.live.Viewport
import org.runestar.client.api.plugins.PluginSettings
import java.awt.Color

class CollisionFlagsDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Canvas.repaints.subscribe { g ->
            g.font = Fonts.PLAIN_12
            g.color = Color.WHITE

            val mouseTile = Viewport.toGame(Mouse.location)?.sceneTile ?: return@subscribe
            val flags = Scene.getCollisionFlags(mouseTile)
            val pt = mouseTile.center.toScreen() ?: return@subscribe
            g.drawString(flags.toString(16), pt)
        })
    }
}
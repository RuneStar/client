package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.*
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.FontForm
import org.kxtra.swing.graphics2d.drawString
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color
import java.awt.Font
import java.awt.Point
import java.awt.Shape
import java.awt.geom.Ellipse2D

class CollisionFlagsDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            g.font = RUNESCAPE_CHAT_FONT
            g.color = Color.WHITE

            val mouseTile = Projections.viewport.toGame(Mouse.location)?.sceneTile ?: return@subscribe
            val flags = LiveScene.getCollisionFlags(mouseTile)
            val pt = mouseTile.center.toScreen() ?: return@subscribe
            g.drawString(flags.toString(16), pt)
        })
    }
}
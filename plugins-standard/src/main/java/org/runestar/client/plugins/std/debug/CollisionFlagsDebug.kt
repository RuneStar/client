package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.*
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.FontForm
import org.kxtra.swing.graphics2d.drawString
import java.awt.Font
import java.awt.Point
import java.awt.Shape
import java.awt.geom.Ellipse2D

class CollisionFlagsDebug : DisposablePlugin<CollisionFlagsDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            g.font = settings.font.get()
            g.color = settings.color.get()

            val mouseTile = Projections.viewport.toGame(Mouse.location)?.sceneTile ?: return@subscribe
            val flags = LiveScene.getCollisionFlags(mouseTile)
            val pt = mouseTile.center.toScreen() ?: return@subscribe
            g.drawString(flags.toString(16), pt)
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}
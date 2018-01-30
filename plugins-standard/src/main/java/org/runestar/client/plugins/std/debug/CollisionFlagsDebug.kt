package org.runestar.client.plugins.std.debug

import org.kxtra.swing.graphics.drawString
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveScene
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.game.api.live.Projections
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color

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
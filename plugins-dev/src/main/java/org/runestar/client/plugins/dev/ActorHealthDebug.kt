package org.runestar.client.plugins.dev

import org.kxtra.swing.graphics.drawString
import org.runestar.client.game.api.Actor
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color

class ActorHealthDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->

            g.font = RUNESCAPE_CHAT_FONT
            g.color = Color.WHITE

            val actors = ArrayList<Actor>()
            actors.addAll(Npcs.toList())
            actors.addAll(Players.toList())

            actors.forEach { actor ->
                val loc = actor.location
                if (!loc.isLoaded) return@forEach
                val pt = loc.center.toScreen() ?: return@forEach
//                val hs = actor.hitSplat ?: return@forEach
//                val hb = actor.healthBar ?: return@forEach
//                val def = hb.definition ?: return@forEach
                val health = actor.health ?: return@forEach
                val string = "$health"

                g.drawString(string, pt)
            }
        })
    }
}
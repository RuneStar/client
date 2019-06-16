package org.runestar.client.plugins.dev

import org.kxtra.swing.graphics.drawString
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Actor
import org.runestar.client.api.Fonts
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class ActorHealthDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(LiveCanvas.repaints.subscribe { g ->

            g.font = Fonts.PLAIN_12
            g.color = Color.WHITE

            val actors = ArrayList<Actor>()
            actors.addAll(Npcs.toList())
            actors.addAll(Players.toList())

            actors.forEach { actor ->
                val loc = actor.location
                if (!loc.isLoaded) return@forEach
                val pt = loc.center.toScreen() ?: return@forEach
//                val hs = actor.hitmark ?: return@forEach
//                val hb = actor.headbar ?: return@forEach
//                val def = hb.type ?: return@forEach
                val health = actor.health ?: return@forEach
                val string = "$health"

                g.drawString(string, pt)
            }
        })
    }
}
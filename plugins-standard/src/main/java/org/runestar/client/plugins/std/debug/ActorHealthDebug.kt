package org.runestar.client.plugins.std.debug

import org.kxtra.swing.graphics2d.drawString
import org.runestar.client.game.api.Actor
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.FontForm
import java.awt.Font

class ActorHealthDebug : DisposablePlugin<ActorHealthDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->

            g.font = settings.font.get()
            g.color = settings.color.get()

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

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm(255, 255, 255)
    }
}
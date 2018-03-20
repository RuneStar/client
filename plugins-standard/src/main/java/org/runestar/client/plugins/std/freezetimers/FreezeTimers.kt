package org.runestar.client.plugins.std.freezetimers

import org.runestar.client.game.api.Actor
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.drawStringShadowed
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color
import java.util.concurrent.ConcurrentHashMap

class FreezeTimers : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name: String = "Freeze Timers"

    val freezes = ConcurrentHashMap<Actor, FreezeState>() // todo: threading

    override fun start() {
        super.start()

        add(Game.ticks.subscribe {

            val itr = freezes.iterator()
            while (itr.hasNext()) {
                val e = itr.next()
                val state = e.value.advance()
                if (state == null) {
                    itr.remove()
                } else {
                    e.setValue(state)
                }
            }

            for (npc in Npcs) {
                processActor(npc)
            }
            for (player in Players) {
                processActor(player)
            }
        })

        add(LiveCanvas.repaints.subscribe { g ->
            g.font = RUNESCAPE_CHAT_FONT
            freezes.forEach { actor, freezeState ->
                val pos = actor.position
                if (!pos.isLoaded) return@forEach
                val pt = pos.copy(height = actor.accessor.defaultHeight).toScreen() ?: return@forEach
                g.color = when (freezeState) {
                    is FreezeState.Frozen -> Color.CYAN
                    is FreezeState.Immune -> Color.PINK
                }
                val s = freezeState.ticksRemaining.toString()
                g.drawStringShadowed(s, pt.x, pt.y)
            }
        })
    }

    fun processActor(actor: Actor) {
        val freeze = freezes[actor]
        if (freeze != null) {
            if (freeze is FreezeState.Frozen && freeze.tile != actor.location) {
                freezes.remove(actor)
            } else {
                return
            }
        }
        val freezeType = FreezeType.fromSpotAnimation(actor.accessor.spotAnimation)
        if (freezeType != null) {
            freezes[actor] = FreezeState.Frozen(freezeType.ticks, actor.location)
        }
    }
}
package org.runestar.client.plugins.std.freezetimers

import org.runestar.client.game.api.Actor
import org.runestar.client.game.api.HeadIconPrayer
import org.runestar.client.game.api.Player
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.drawStringShadowed
import org.runestar.general.fonts.RUNESCAPE_SMALL_FONT
import java.awt.Color
import java.util.concurrent.ConcurrentHashMap

class FreezeTimers : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name: String = "Freeze Timers"

    private val freezes = ConcurrentHashMap<Actor, FreezeState>() // todo: threading

    override fun start() {
        super.start()

        add(Game.ticks.subscribe {

            tickFreezes()

            for (npc in Npcs) {
                processActor(npc)
            }

            for (player in Players) {
                processActor(player)
            }
        })

        add(LiveCanvas.repaints.subscribe { g ->
            g.font = RUNESCAPE_SMALL_FONT
            freezes.forEach { actor, freezeState ->
                if (!actor.accessor.isVisible) return@forEach
                val pos = actor.position
                if (!pos.isLoaded) return@forEach
                val height = actor.accessor.defaultHeight * 2 / 3
                val pt = pos.copy(height = height).toScreen() ?: return@forEach
                g.color = when (freezeState) {
                    is FreezeState.Frozen -> Color.CYAN
                    is FreezeState.Immune -> Color.PINK
                }
                val s = freezeState.ticksRemaining.toString()
                g.drawStringShadowed(s, pt.x, pt.y)
            }
        })
    }

    private fun processActor(actor: Actor) {
        val existingFreeze = freezes[actor]
        if (existingFreeze == null) {
            val freezeType = FreezeType.fromSpotAnimation(actor.accessor.spotAnimation)
            if (freezeType != null) {
                freezes[actor] = freezeStateForActor(actor, freezeType)
            }
        }
    }

    private fun freezeStateForActor(actor: Actor, freezeType: FreezeType): FreezeState {
        var ticks = freezeType.ticks
        if (
                freezeType.halfOnPray &&
                actor is Player &&
                actor.headIconPrayer == HeadIconPrayer.PROTECT_FROM_MAGIC
        ) {
            ticks /= 2
        }
        return FreezeState.Frozen(ticks)
    }

    private fun tickFreezes() {
        val itr = freezes.iterator()
        while (itr.hasNext()) {
            val e = itr.next()
            val nextState = e.value.nextTick()
            if (nextState == null) {
                itr.remove()
            } else {
                e.setValue(nextState)
            }
        }
    }
}
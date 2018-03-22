package org.runestar.client.plugins.std.freezetimers

import org.runestar.client.game.api.*
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.drawStringShadowed
import org.runestar.general.fonts.RUNESCAPE_SMALL_FONT
import java.awt.Color
import java.awt.Graphics2D
import java.util.concurrent.ConcurrentHashMap

class FreezeTimers : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name: String = "Freeze Timers"

    private val playerFreezes = ConcurrentHashMap<String, FreezeState>()

    private val npcFreezes = ConcurrentHashMap<Npc, FreezeState>()

    override fun start() {
        super.start()

        add(Game.ticks.subscribe {

            tickFreezes(npcFreezes)
            tickFreezes(playerFreezes)

            for (npc in Npcs) {
                processActor(npc, npc, npcFreezes)
            }

            for (player in Players) {
                val name = player.name?.name ?: continue
                processActor(player, name, playerFreezes)
            }
        })

        add(LiveCanvas.repaints.subscribe { g ->
            g.font = RUNESCAPE_SMALL_FONT

            for (npc in Npcs) {
                val freezeState = npcFreezes[npc] ?: continue
                drawActor(g, npc, freezeState)
            }

            for (player in Players) {
                val name = player.name?.name ?: continue
                val freezeState = playerFreezes[name] ?: continue
                drawActor(g, player, freezeState)
            }
        })
    }

    private fun drawActor(
            g: Graphics2D,
            actor: Actor,
            freezeState: FreezeState
    ) {
        val pos = actor.position
        if (!pos.isLoaded) return
        val height = actor.accessor.defaultHeight * 2 / 3
        val pt = pos.copy(height = height).toScreen() ?: return
        g.color = when (freezeState) {
            is FreezeState.Frozen -> Color.CYAN
            is FreezeState.Immune -> Color.PINK
        }
        val s = freezeState.ticksRemaining.toString()
        g.drawStringShadowed(s, pt.x, pt.y)
    }

    private fun <T> processActor(
            actor: Actor,
            key: T,
            freezes: MutableMap<T, FreezeState>
    ) {
        if (actor.accessor.walkSequence == -1 && actor.accessor.runSequence == -1) {
            // npc which can't move, like a castle wars barricade
            return
        }
        if (actor.accessor.sequence == SequenceId.HUMAN_DYING) {
            freezes.remove(key)
            return
        }
        val existingFreeze = freezes[key]
        if (existingFreeze != null) {
            if (existingFreeze is FreezeState.Frozen) {
                val mvmt = actor.accessor.movementSequence
                if (mvmt == actor.accessor.walkSequence || mvmt == actor.accessor.runSequence) {
                    freezes.remove(key)
                }
            }
            return
        }
        val freezeType = FreezeType.fromSpotAnimation(actor.accessor.spotAnimation)
        if (freezeType != null) {
            freezes[key] = freezeStateForActor(actor, freezeType)
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

    private fun tickFreezes(freezes: MutableMap<*, FreezeState>) {
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
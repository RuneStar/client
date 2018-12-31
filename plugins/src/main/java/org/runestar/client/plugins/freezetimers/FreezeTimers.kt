package org.runestar.client.plugins.freezetimers

import org.runestar.client.api.Fonts
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.util.drawStringShadowed
import org.runestar.client.game.api.Actor
import org.runestar.client.game.api.HeadIconPrayer
import org.runestar.client.game.api.Player
import org.runestar.client.game.api.SequenceId
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D

class FreezeTimers : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name: String = "Freeze Timers"

    private val playerFreezes: MutableMap<String, FreezeState> = LinkedHashMap()

    private val npcFreezes: MutableMap<Int, FreezeState> = LinkedHashMap()

    private var loadedFrozenActors: List<Pair<Actor, FreezeState>> = ArrayList()

    override fun onStart() {
        add(Game.ticks.subscribe { onTick() })
        add(LiveCanvas.repaints.subscribe(::onRepaint))
    }

    private fun onTick() {
        tickFreezes(npcFreezes)
        tickFreezes(playerFreezes)

        val loaded = ArrayList<Pair<Actor, FreezeState>>()

        for (npc in Npcs) {
            processActor(npc, npc.index, npcFreezes, loaded)
        }

        for (player in Players) {
            val name = player.name?.name ?: continue
            processActor(player, name, playerFreezes, loaded)
        }

        loadedFrozenActors = loaded
    }

    private fun onRepaint(g: Graphics2D) {
        g.font = Fonts.PLAIN_11
        for ((actor, freezeState) in loadedFrozenActors) {
            drawActor(g, actor, freezeState)
        }
    }

    private fun drawActor(
            g: Graphics2D,
            actor: Actor,
            freezeState: FreezeState
    ) {
        val pos = actor.modelPosition
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
            freezes: MutableMap<T, FreezeState>,
            loaded: MutableList<Pair<Actor, FreezeState>>
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
            if (existingFreeze is FreezeState.Frozen && actor.accessor.movementSequence == actor.accessor.runSequence) {
                // player is thought to be frozen, but this is proved to be wrong because he is running
                // cannot test walking because of d spears
                freezes.remove(key)
            } else {
                loaded.add(actor to existingFreeze)
            }
            return
        }
        val freezeType = FreezeType.fromSpotAnimation(actor.accessor.spotAnimation)
        if (freezeType != null) {
            val freezeState = freezeStateForActor(actor, freezeType)
            freezes[key] = freezeState
            loaded.add(actor to freezeState)
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
        if (freezes.isEmpty()) return
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
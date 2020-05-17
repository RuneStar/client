package org.runestar.client.api.game

import org.runestar.client.api.game.live.Npcs
import org.runestar.client.api.game.live.Players

interface ActorTargeting {

    val target: Actor? get() = targetNpc ?: targetPlayer

    val targetNpc: Npc? get() = npcTargetIndex.let { if (it == -1) null else Npcs[it] }

    val targetPlayer: Player? get() = playerTargetIndex.let { if (it == -1) null else Players[it] }

    val hasTarget: Boolean get() = npcTargetIndex != -1 || playerTargetIndex != -1

    val npcTargetIndex: Int

    val playerTargetIndex: Int
}
package org.runestar.client.game.api

import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players

interface ActorTargeting {

    val target: Actor? get() = npcTargetIndex?.let { Npcs[it] } ?: playerTargetIndex?.let { Players[it] }

    val hasTarget get() = npcTargetIndex != null || playerTargetIndex != null

    val npcTargetIndex: Int?

    val playerTargetIndex: Int?
}
package com.runesuite.client.game.api

import com.runesuite.client.game.api.live.Npcs
import com.runesuite.client.game.api.live.Players

interface ActorTargeting {

    val target: Actor? get() = npcTargetIndex?.let { Npcs[it] } ?: playerTargetIndex?.let { Players[it] }

    val hasTarget get() = npcTargetIndex != null || playerTargetIndex != null

    val npcTargetIndex: Int?

    val playerTargetIndex: Int?
}
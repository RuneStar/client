package com.runesuite.client.api

import com.runesuite.client.api.live.Npcs
import com.runesuite.client.api.live.Players

interface ActorTargeting {

    val target: Actor? get() = npcTargetIndex?.let { Npcs[it] } ?: playerTargetIndex?.let { Players[it] }

    val hasTarget get() = npcTargetIndex != null || playerTargetIndex != null

    val npcTargetIndex: Int?

    val playerTargetIndex: Int?
}
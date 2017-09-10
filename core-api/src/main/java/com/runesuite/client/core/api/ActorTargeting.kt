package com.runesuite.client.core.api

import com.runesuite.client.core.api.live.Npcs
import com.runesuite.client.core.api.live.Players

interface ActorTargeting {

    val target: Actor? get() = npcTargetIndex?.let { Npcs[it] } ?: playerTargetIndex?.let { Players[it] }

    val hasTarget get() = npcTargetIndex != null || playerTargetIndex != null

    val npcTargetIndex: Int?

    val playerTargetIndex: Int?
}
package com.runesuite.client.game

import com.runesuite.client.game.live.Npcs
import com.runesuite.client.game.live.Players

interface ActorTargeting {

    val target: Actor? get() = (npcTargetIndex?.let { Npcs[it] } ?:
            playerTargetIndex?.let { Players[it] })

    val hasTarget get() = npcTargetIndex != null || playerTargetIndex != null

    val npcTargetIndex: Int?

    val playerTargetIndex: Int?
}
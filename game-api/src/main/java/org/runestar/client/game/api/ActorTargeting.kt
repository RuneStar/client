package org.runestar.client.game.api

import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players

interface ActorTargeting {

    val target: Actor? get() {
        var i = npcTargetIndex
        if (i != -1) return Npcs[i]
        i = playerTargetIndex
        if (i != -1) return Players[i]
        return null
    }

    val hasTarget: Boolean get() = npcTargetIndex != -1 || playerTargetIndex != -1

    val npcTargetIndex: Int

    val playerTargetIndex: Int
}
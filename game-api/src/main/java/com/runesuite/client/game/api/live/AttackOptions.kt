package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.AttackOption
import com.runesuite.client.game.raw.Client.accessor

object AttackOptions {

    val npc get() = AttackOption.LOOKUP.getValue(accessor.npcAttackOption)

    val player get() = AttackOption.LOOKUP.getValue(accessor.playerAttackOption)
}
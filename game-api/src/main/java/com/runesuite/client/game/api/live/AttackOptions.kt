package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.AttackOption
import com.runesuite.client.game.raw.Client

object AttackOptions {

    val npc get() = AttackOption.get(Client.accessor.npcAttackOption)

    val player get() = AttackOption.get(Client.accessor.playerAttackOption)
}
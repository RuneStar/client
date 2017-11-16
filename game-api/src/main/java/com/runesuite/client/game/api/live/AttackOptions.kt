package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.AttackOption
import com.runesuite.client.game.raw.Client.accessor

object AttackOptions {

    val npc get() = checkNotNull(AttackOption.LOOKUP[accessor.npcAttackOption]) { accessor.npcAttackOption }

    val player get() = checkNotNull(AttackOption.LOOKUP[accessor.playerAttackOption]) { accessor.playerAttackOption }
}
package org.runestar.client.game.api.live

import org.runestar.client.game.api.AttackOption
import org.runestar.client.game.raw.Client.accessor

object AttackOptions {

    val npc get() = AttackOption.LOOKUP.getValue(accessor.npcAttackOption)

    val player get() = AttackOption.LOOKUP.getValue(accessor.playerAttackOption)
}
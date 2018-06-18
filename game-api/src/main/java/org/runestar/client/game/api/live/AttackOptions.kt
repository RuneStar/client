package org.runestar.client.game.api.live

import org.runestar.client.game.api.AttackOption
import org.runestar.client.game.raw.CLIENT

object AttackOptions {

    val npc get() = AttackOption.of(CLIENT.npcAttackOption)

    val player get() = AttackOption.of(CLIENT.playerAttackOption)
}
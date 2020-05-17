package org.runestar.client.api.game.live

import org.runestar.client.api.game.AttackOptionId
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XAttackOption
import java.lang.IllegalArgumentException

object AttackOptions {

    var npc: Int
        get() = CLIENT.npcAttackOption.id
        set(value) { CLIENT.npcAttackOption = of(value) }

    var player: Int
        get() = CLIENT.playerAttackOption.id
        set(value) { CLIENT.playerAttackOption = of(value) }

    private fun of(id: Int): XAttackOption = when (id) {
        AttackOptionId.DEPENDS_ON_COMBAT_LEVELS -> CLIENT.attackOption_dependsOnCombatLevels
        AttackOptionId.ALWAYS_RIGHT_CLICK -> CLIENT.attackOption_alwaysRightClick
        AttackOptionId.LEFT_CLICK_WHERE_AVAILABLE -> CLIENT.attackOption_leftClickWhereAvailable
        AttackOptionId.HIDDEN -> CLIENT.attackOption_hidden
        else -> throw IllegalArgumentException(id.toString())
    }
}
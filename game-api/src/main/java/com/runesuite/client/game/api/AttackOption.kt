package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.game.raw.Wrapped
import com.runesuite.client.game.raw.access.XAttackOption

enum class AttackOption(override val accessor: XAttackOption) : Wrapped {

    ALWAYS_RIGHT_CLICK(accessor.attackOption_alwaysRightClick),
    DEPENDS_ON_COMBAT_LEVELS(accessor.attackOption_dependsOnCombatLevels),
    HIDDEN(accessor.attackOption_hidden),
    LEFT_CLICK_WHERE_AVAILABLE(accessor.attackOption_leftClickWhereAvailable);

    companion object {

        @JvmField
        val LOOKUP = values().associateBy { it.accessor }
    }
}
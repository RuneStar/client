package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.access.XAttackOption

enum class AttackOption {

    ALWAYS_RIGHT_CLICK,
    DEPENDS_ON_COMBAT_LEVELS,
    HIDDEN,
    LEFT_CLICK_WHERE_AVAILABLE;

    companion object {
        @JvmStatic
        fun get(accessor: XAttackOption): AttackOption {
            return when (accessor) {
                Client.accessor.attackOption_alwaysRightClick -> ALWAYS_RIGHT_CLICK
                Client.accessor.attackOption_dependsOnCombatLevels -> DEPENDS_ON_COMBAT_LEVELS
                Client.accessor.attackOption_hidden -> HIDDEN
                Client.accessor.attackOption_leftClickWhereAvailable -> LEFT_CLICK_WHERE_AVAILABLE
                else -> error(accessor)
            }
        }
    }
}
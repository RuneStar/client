package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XAttackOption
import org.runestar.client.game.raw.CLIENT

enum class AttackOption(val accessor: XAttackOption) {

    ALWAYS_RIGHT_CLICK(CLIENT.attackOption_alwaysRightClick),
    DEPENDS_ON_COMBAT_LEVELS(CLIENT.attackOption_dependsOnCombatLevels),
    HIDDEN(CLIENT.attackOption_hidden),
    LEFT_CLICK_WHERE_AVAILABLE(CLIENT.attackOption_leftClickWhereAvailable);

    companion object {

        @JvmField val LOOKUP = values().associateBy { it.accessor }

        fun of(accessor: XAttackOption): AttackOption {
            return LOOKUP.getValue(accessor)
        }
    }
}
package org.runestar.client.game.api.live

import org.runestar.client.game.api.Prayer

object Prayers {

    fun isEnabled(prayer: Prayer) = Vars.getVarbit(prayer.varbit) == 1
}
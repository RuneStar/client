package org.runestar.client.game.api.live

import org.runestar.client.game.api.EnumId
import org.runestar.client.game.api.VarbitId
import org.runestar.client.game.api.utils.BitVec

object Prayers {

    val enabled: BitVec get() = BitVec(Vars.getVarbit(VarbitId.PRAYERS_ENABLED))

    val quickPrayers: BitVec get() = BitVec(Vars.getVarbit(VarbitId.QUICK_PRAYERS))

    fun name(prayer: Int): String = Enums[EnumId.PRAYER_NAMES].getString(prayer)
}
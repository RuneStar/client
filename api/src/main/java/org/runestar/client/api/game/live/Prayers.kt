package org.runestar.client.api.game.live

import org.runestar.client.api.game.EnumId
import org.runestar.client.api.game.VarbitId
import org.runestar.client.api.util.BitVec

object Prayers {

    val enabled: BitVec get() = BitVec(Vars.getVarbit(VarbitId.PRAYERS_ENABLED))

    val quickPrayers: BitVec get() = BitVec(Vars.getVarbit(VarbitId.QUICK_PRAYERS))

    fun name(prayer: Int): String = Enums[EnumId.PRAYER_NAMES].getString(prayer)
}
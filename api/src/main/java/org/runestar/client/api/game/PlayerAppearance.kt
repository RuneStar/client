package org.runestar.client.api.game

import org.runestar.client.raw.access.XPlayerAppearance

inline class PlayerAppearance(val accessor: XPlayerAppearance) {

    fun item(equipmentSlot: Int) : Int {
        val v = accessor.equipment[equipmentSlot]
        return if (v >= 512) v - 512 else -1
    }

    fun kit(equipmentSlot: Int) : Int {
        val v = accessor.equipment[equipmentSlot]
        return if (v in 256..511) v - 256 else -1
    }

    val bodyColors: IntArray get() = accessor.bodyColors

    val sex get() = if (accessor.isFemale) Sex.FEMALE else Sex.MALE
}
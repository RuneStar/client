package org.runestar.client.game.api

data class Varbit(
        val index: Int,
        val lowBit: Int,
        val highBit: Int
) {

    init {
        require(lowBit <= highBit &&
                lowBit in 0 until Integer.SIZE &&
                highBit in 0 until Integer.SIZE &&
                index >= 0)
    }

    val bitLength: Int get() = highBit - lowBit + 1

    val max: Int get() = (1 shl bitLength) - 1

    companion object {

        @JvmField val PRAYER_THICK_SKIN = Varbit(83, 0, 0)
        @JvmField val PRAYER_BURST_OF_STRENGTH = Varbit(83, 1, 1)
        @JvmField val PRAYER_CLARITY_OF_THOUGHT = Varbit(83, 2, 2)
        @JvmField val PRAYER_SHARP_EYE = Varbit(83, 18, 18)
        @JvmField val PRAYER_MYSTIC_WILL = Varbit(83, 19, 19)
        @JvmField val PRAYER_ROCK_SKIN = Varbit(83, 3, 3)
        @JvmField val PRAYER_SUPERHUMAN_STRENGTH = Varbit(83, 4, 4)
        @JvmField val PRAYER_IMPROVED_REFLEXES = Varbit(83, 5, 5)
        @JvmField val PRAYER_RAPID_RESTORE = Varbit(83, 6, 6)
        @JvmField val PRAYER_RAPID_HEAL = Varbit(83, 7, 7)
        @JvmField val PRAYER_PROTECT_ITEM = Varbit(83, 8, 8)
        @JvmField val PRAYER_HAWK_EYE = Varbit(83, 20, 20)
        @JvmField val PRAYER_MYSTIC_LORE = Varbit(83, 21, 21)
        @JvmField val PRAYER_STEEL_SKIN = Varbit(83, 9, 9)
        @JvmField val PRAYER_ULTIMATE_STRENGTH = Varbit(83, 10, 10)
        @JvmField val PRAYER_INCREDIBLE_REFLEXES = Varbit(83, 11, 11)
        @JvmField val PRAYER_PROTECT_FROM_MAGIC = Varbit(83, 12, 12)
        @JvmField val PRAYER_PROTECT_FROM_MISSILES = Varbit(83, 13, 13)
        @JvmField val PRAYER_PROTECT_FROM_MELEE = Varbit(83, 14, 14)
        @JvmField val PRAYER_EAGLE_EYE = Varbit(83, 22, 22)
        @JvmField val PRAYER_MYSTIC_MIGHT = Varbit(83, 23, 23)
        @JvmField val PRAYER_RETRIBUTION = Varbit(83, 15, 15)
        @JvmField val PRAYER_REDEMPTION = Varbit(83, 16, 16)
        @JvmField val PRAYER_SMITE = Varbit(83, 17, 17)
        @JvmField val PRAYER_CHIVALRY = Varbit(83, 25, 25)
        @JvmField val PRAYER_PIETY = Varbit(83, 26, 26)
        @JvmField val PRAYER_PRESERVE = Varbit(83, 28, 28)
    }
}
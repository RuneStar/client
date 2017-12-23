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

    enum class Default(val varbit: Varbit, val id: Int = -1) {

        PRAYER_THICK_SKIN(Varbit(83, 0, 0), 4104),
        PRAYER_BURST_OF_STRENGTH(Varbit(83, 1, 1), 4105),
        PRAYER_CLARITY_OF_THOUGHT(Varbit(83, 2, 2), 4106),
        PRAYER_SHARP_EYE(Varbit(83, 18, 18), 4122),
        PRAYER_MYSTIC_WILL(Varbit(83, 19, 19), 4123),
        PRAYER_ROCK_SKIN(Varbit(83, 3, 3), 4107),
        PRAYER_SUPERHUMAN_STRENGTH(Varbit(83, 4, 4), 4108),
        PRAYER_IMPROVED_REFLEXES(Varbit(83, 5, 5), 4109),
        PRAYER_RAPID_RESTORE(Varbit(83, 6, 6), 4110),
        PRAYER_RAPID_HEAL(Varbit(83, 7, 7), 4111),
        PRAYER_PROTECT_ITEM(Varbit(83, 8, 8), 4112),
        PRAYER_HAWK_EYE(Varbit(83, 20, 20), 4124),
        PRAYER_MYSTIC_LORE(Varbit(83, 21, 21), 4125),
        PRAYER_STEEL_SKIN(Varbit(83, 9, 9), 4113),
        PRAYER_ULTIMATE_STRENGTH(Varbit(83, 10, 10), 4114),
        PRAYER_INCREDIBLE_REFLEXES(Varbit(83, 11, 11), 4115),
        PRAYER_PROTECT_FROM_MAGIC(Varbit(83, 12, 12), 4116),
        PRAYER_PROTECT_FROM_MISSILES(Varbit(83, 13, 13), 4116),
        PRAYER_PROTECT_FROM_MELEE(Varbit(83, 14, 14), 4118),
        PRAYER_EAGLE_EYE(Varbit(83, 22, 22), 4126),
        PRAYER_MYSTIC_MIGHT(Varbit(83, 23, 23), 4127),
        PRAYER_RETRIBUTION(Varbit(83, 15, 15), 4119),
        PRAYER_REDEMPTION(Varbit(83, 16, 16), 4120),
        PRAYER_SMITE(Varbit(83, 17, 17), 4121),
        PRAYER_CHIVALRY(Varbit(83, 25, 25), 4128),
        PRAYER_PIETY(Varbit(83, 26, 26), 4129),
        PRAYER_PRESERVE(Varbit(83, 28, 28), 5466),
    }
}
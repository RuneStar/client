package com.runesuite.client.game.api

// todo : drain rate
enum class Prayer(
        val level: Int,
        val isMembersOnly: Boolean,
        val defaultVarbit: Varbit.Default
) {
    THICK_SKIN(1, false, Varbit.Default.PRAYER_THICK_SKIN),
    BURST_OF_STRENGTH(4, false, Varbit.Default.PRAYER_BURST_OF_STRENGTH),
    CLARITY_OF_THOUGHT(7, false, Varbit.Default.PRAYER_CLARITY_OF_THOUGHT),
    SHARP_EYE(8, false, Varbit.Default.PRAYER_SHARP_EYE),
    MYSTIC_WILL(9, false, Varbit.Default.PRAYER_MYSTIC_MIGHT),
    ROCK_SKIN(10, false, Varbit.Default.PRAYER_ROCK_SKIN),
    SUPERHUMAN_STRENGTH(13, false, Varbit.Default.PRAYER_SUPERHUMAN_STRENGTH),
    IMPROVED_REFLEXES(16, false, Varbit.Default.PRAYER_IMPROVED_REFLEXES),
    RAPID_RESTORE(19, false, Varbit.Default.PRAYER_RAPID_RESTORE),
    RAPID_HEAL(22, false, Varbit.Default.PRAYER_RAPID_HEAL),
    PROTECT_ITEM(25, false, Varbit.Default.PRAYER_PROTECT_ITEM),
    HAWK_EYE(26, false, Varbit.Default.PRAYER_HAWK_EYE),
    MYSTIC_LORE(27, false, Varbit.Default.PRAYER_MYSTIC_LORE),
    STEEL_SKIN(28, false, Varbit.Default.PRAYER_STEEL_SKIN),
    ULTIMATE_STRENGTH(31, false, Varbit.Default.PRAYER_ULTIMATE_STRENGTH),
    INCREDIBLE_REFLEXES(34, false, Varbit.Default.PRAYER_INCREDIBLE_REFLEXES),
    PROTECT_FROM_MAGIC(37, false, Varbit.Default.PRAYER_PROTECT_FROM_MAGIC),
    PROTECT_FROM_MISSILES(40, false, Varbit.Default.PRAYER_PROTECT_FROM_MISSILES),
    PROTECT_FROM_MELEE(43, false, Varbit.Default.PRAYER_PROTECT_FROM_MELEE),
    EAGLE_EYE(44, false, Varbit.Default.PRAYER_EAGLE_EYE),
    MYSTIC_MIGHT(45, false, Varbit.Default.PRAYER_MYSTIC_MIGHT),
    RETRIBUTION(46, true, Varbit.Default.PRAYER_RETRIBUTION),
    REDEMPTION(49, true, Varbit.Default.PRAYER_REDEMPTION),
    SMITE(52, true, Varbit.Default.PRAYER_SMITE),
    PRESERVE(55, true, Varbit.Default.PRAYER_PRESERVE),
    CHIVALRY(60, true, Varbit.Default.PRAYER_CHIVALRY),
    PIETY(70, true, Varbit.Default.PRAYER_PIETY);
    // rigour 74
    // augury 77
}
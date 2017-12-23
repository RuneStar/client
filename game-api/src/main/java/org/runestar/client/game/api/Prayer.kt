package org.runestar.client.game.api

// todo : drain rate
enum class Prayer(
        val level: Int,
        val isMembersOnly: Boolean,
        val varbit: Varbit
) {
    THICK_SKIN(1, false, Varbit.PRAYER_THICK_SKIN),
    BURST_OF_STRENGTH(4, false, Varbit.PRAYER_BURST_OF_STRENGTH),
    CLARITY_OF_THOUGHT(7, false, Varbit.PRAYER_CLARITY_OF_THOUGHT),
    SHARP_EYE(8, false, Varbit.PRAYER_SHARP_EYE),
    MYSTIC_WILL(9, false, Varbit.PRAYER_MYSTIC_WILL),
    ROCK_SKIN(10, false, Varbit.PRAYER_ROCK_SKIN),
    SUPERHUMAN_STRENGTH(13, false, Varbit.PRAYER_SUPERHUMAN_STRENGTH),
    IMPROVED_REFLEXES(16, false, Varbit.PRAYER_IMPROVED_REFLEXES),
    RAPID_RESTORE(19, false, Varbit.PRAYER_RAPID_RESTORE),
    RAPID_HEAL(22, false, Varbit.PRAYER_RAPID_HEAL),
    PROTECT_ITEM(25, false, Varbit.PRAYER_PROTECT_ITEM),
    HAWK_EYE(26, false, Varbit.PRAYER_HAWK_EYE),
    MYSTIC_LORE(27, false, Varbit.PRAYER_MYSTIC_LORE),
    STEEL_SKIN(28, false, Varbit.PRAYER_STEEL_SKIN),
    ULTIMATE_STRENGTH(31, false, Varbit.PRAYER_ULTIMATE_STRENGTH),
    INCREDIBLE_REFLEXES(34, false, Varbit.PRAYER_INCREDIBLE_REFLEXES),
    PROTECT_FROM_MAGIC(37, false, Varbit.PRAYER_PROTECT_FROM_MAGIC),
    PROTECT_FROM_MISSILES(40, false, Varbit.PRAYER_PROTECT_FROM_MISSILES),
    PROTECT_FROM_MELEE(43, false, Varbit.PRAYER_PROTECT_FROM_MELEE),
    EAGLE_EYE(44, false, Varbit.PRAYER_EAGLE_EYE),
    MYSTIC_MIGHT(45, false, Varbit.PRAYER_MYSTIC_MIGHT),
    RETRIBUTION(46, true, Varbit.PRAYER_RETRIBUTION),
    REDEMPTION(49, true, Varbit.PRAYER_REDEMPTION),
    SMITE(52, true, Varbit.PRAYER_SMITE),
    PRESERVE(55, true, Varbit.PRAYER_PRESERVE),
    CHIVALRY(60, true, Varbit.PRAYER_CHIVALRY),
    PIETY(70, true, Varbit.PRAYER_PIETY);
    // rigour 74
    // augury 77
}
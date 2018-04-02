package org.runestar.client.game.api

// todo : drain rate https://twitter.com/JagexAsh/status/814540902542802944
enum class Prayer(
        val level: Int,
        val isMembersOnly: Boolean,
        val varbit: Int
) {
    THICK_SKIN(1, false, VarbitId.PRAYER_THICK_SKIN),
    BURST_OF_STRENGTH(4, false, VarbitId.PRAYER_BURST_OF_STRENGTH),
    CLARITY_OF_THOUGHT(7, false, VarbitId.PRAYER_CLARITY_OF_THOUGHT),
    SHARP_EYE(8, false, VarbitId.PRAYER_SHARP_EYE),
    MYSTIC_WILL(9, false, VarbitId.PRAYER_MYSTIC_WILL),
    ROCK_SKIN(10, false, VarbitId.PRAYER_ROCK_SKIN),
    SUPERHUMAN_STRENGTH(13, false, VarbitId.PRAYER_SUPERHUMAN_STRENGTH),
    IMPROVED_REFLEXES(16, false, VarbitId.PRAYER_IMPROVED_REFLEXES),
    RAPID_RESTORE(19, false, VarbitId.PRAYER_RAPID_RESTORE),
    RAPID_HEAL(22, false, VarbitId.PRAYER_RAPID_HEAL),
    PROTECT_ITEM(25, false, VarbitId.PRAYER_PROTECT_ITEM),
    HAWK_EYE(26, false, VarbitId.PRAYER_HAWK_EYE),
    MYSTIC_LORE(27, false, VarbitId.PRAYER_MYSTIC_LORE),
    STEEL_SKIN(28, false, VarbitId.PRAYER_STEEL_SKIN),
    ULTIMATE_STRENGTH(31, false, VarbitId.PRAYER_ULTIMATE_STRENGTH),
    INCREDIBLE_REFLEXES(34, false, VarbitId.PRAYER_INCREDIBLE_REFLEXES),
    PROTECT_FROM_MAGIC(37, false, VarbitId.PRAYER_PROTECT_FROM_MAGIC),
    PROTECT_FROM_MISSILES(40, false, VarbitId.PRAYER_PROTECT_FROM_MISSILES),
    PROTECT_FROM_MELEE(43, false, VarbitId.PRAYER_PROTECT_FROM_MELEE),
    EAGLE_EYE(44, false, VarbitId.PRAYER_EAGLE_EYE),
    MYSTIC_MIGHT(45, false, VarbitId.PRAYER_MYSTIC_MIGHT),
    RETRIBUTION(46, true, VarbitId.PRAYER_RETRIBUTION),
    REDEMPTION(49, true, VarbitId.PRAYER_REDEMPTION),
    SMITE(52, true, VarbitId.PRAYER_SMITE),
    PRESERVE(55, true, VarbitId.PRAYER_PRESERVE),
    CHIVALRY(60, true, VarbitId.PRAYER_CHIVALRY),
    PIETY(70, true, VarbitId.PRAYER_PIETY),
    RIGOUR(74, true, VarbitId.PRAYER_RIGOUR),
    AUGURY(77, true, VarbitId.PRAYER_AUGURY);

    companion object {

        @JvmField val VALUES = values().asList()
    }
}
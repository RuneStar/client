package org.runestar.client.game.api

enum class HeadIconPrayer(val id: Int) {

    PROTECT_FROM_MELEE(0),
    PROTECT_FROM_MISSILES(1),
    PROTECT_FROM_MAGIC(2),
    RETRIBUTION(3),
    SMITE(4),
    REDEMPTION(5),
    PROTECT_FROM_MAGIC_AND_MISSILES(6);

    companion object {
        val LOOKUP = values().associateBy { it.id }
    }
}
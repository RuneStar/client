package org.runestar.client.game.api

enum class HeadIconPrayer(
        val id: Int,
        val prayer: Prayer?
) {
    PROTECT_FROM_MELEE(0, Prayer.PROTECT_FROM_MELEE),
    PROTECT_FROM_MISSILES(1, Prayer.PROTECT_FROM_MISSILES),
    PROTECT_FROM_MAGIC(2, Prayer.PROTECT_FROM_MAGIC),
    RETRIBUTION(3, Prayer.RETRIBUTION),
    SMITE(4, Prayer.SMITE),
    REDEMPTION(5, Prayer.REDEMPTION),
    PROTECT_FROM_MAGIC_AND_MISSILES(6, null);

    companion object {

        @JvmField val VALUES = values().asList()

        fun of(id: Int): HeadIconPrayer? {
            if (id == -1) return null
            return VALUES[id]
        }
    }
}
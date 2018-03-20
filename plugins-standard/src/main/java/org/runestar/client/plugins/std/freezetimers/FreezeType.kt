package org.runestar.client.plugins.std.freezetimers

enum class FreezeType(
        val ticks: Int,
        val spotAnimation: Int,
        val halfOnPray: Boolean
) {
    ICE_RUSH(8, 361, false),
    ICE_BURST(16, 363, false),
    ICE_BLITZ(24, 367, false),
    ICE_BARRAGE(32, 369, false),

    BIND(8, 181, true),
    SNARE(16, 180, true),
    ENTANGLE(24, 179, true);

    companion object {

        private val LOOKUP = values().associateBy { it.spotAnimation }

        fun fromSpotAnimation(spotAnimation: Int): FreezeType? {
            return LOOKUP[spotAnimation]
        }
    }
}
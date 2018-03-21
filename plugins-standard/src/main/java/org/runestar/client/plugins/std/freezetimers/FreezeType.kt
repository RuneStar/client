package org.runestar.client.plugins.std.freezetimers

import org.runestar.client.game.api.SpotAnimationId

enum class FreezeType(
        val ticks: Int,
        val spotAnimation: Int,
        val halfOnPray: Boolean
) {
    ICE_RUSH(8, SpotAnimationId.ICE_RUSH, false),
    ICE_BURST(16, SpotAnimationId.ICE_BURST, false),
    ICE_BLITZ(24, SpotAnimationId.ICE_BLITZ, false),
    ICE_BARRAGE(32, SpotAnimationId.ICE_BARRAGE, false),

    BIND(8, SpotAnimationId.BIND, true),
    SNARE(16, SpotAnimationId.SNARE, true),
    ENTANGLE(24, SpotAnimationId.ENTANGLE, true);

    companion object {

        private val LOOKUP = values().associateBy { it.spotAnimation }

        fun fromSpotAnimation(spotAnimation: Int): FreezeType? {
            return LOOKUP[spotAnimation]
        }
    }
}
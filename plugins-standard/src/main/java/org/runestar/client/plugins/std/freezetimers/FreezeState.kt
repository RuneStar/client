package org.runestar.client.plugins.std.freezetimers

import org.runestar.client.game.api.SceneTile

sealed class FreezeState() {

    abstract val ticksRemaining: Int

    abstract fun advance(): FreezeState?

    data class Frozen(override val ticksRemaining: Int, val tile: SceneTile) : FreezeState() {

        override fun advance(): FreezeState {
            val n = ticksRemaining
            return if (n == 0) {
                Immune()
            } else {
                Frozen(n - 1, tile)
            }
        }
    }

    data class Immune(override val ticksRemaining: Int = 6) : FreezeState() {

        override fun advance(): FreezeState? {
            val n = ticksRemaining
            return if (n == 0) {
                null
            } else {
                Immune(n -1 )
            }
        }
    }
}
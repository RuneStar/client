package org.runestar.client.plugins.std.freezetimers

sealed class FreezeState {

    abstract val ticksRemaining: Int

    abstract fun nextTick(): FreezeState?

    data class Frozen(override val ticksRemaining: Int) : FreezeState() {

        override fun nextTick(): FreezeState {
            val n = ticksRemaining
            return if (n == 0) {
                Immune()
            } else {
                Frozen(n - 1)
            }
        }
    }

    data class Immune(override val ticksRemaining: Int = 6) : FreezeState() {

        override fun nextTick(): FreezeState? {
            val n = ticksRemaining
            return if (n == 0) {
                null
            } else {
                Immune(n - 1)
            }
        }
    }
}
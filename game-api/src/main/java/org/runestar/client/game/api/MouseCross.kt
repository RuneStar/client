package org.runestar.client.game.api

data class MouseCross(
        val color: MouseCrossColor,
        val state: Int
) {

    companion object {

        @JvmStatic
        fun of(color: MouseCrossColor?, state: Int): MouseCross? {
            if (color == null) return null
            return MouseCross(color, state)
        }
    }
}
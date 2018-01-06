package org.runestar.client.game.api

enum class MouseCrossColor(val id: Int) {

    YELLOW(1),
    RED(2);

    companion object {

        @JvmField val LOOKUP = values().associateBy { it.id }

        @JvmStatic
        fun of(id: Int): MouseCrossColor? {
            if (id == 0) return null
            return LOOKUP.getValue(id)
        }
    }
}
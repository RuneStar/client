package org.runestar.client.game.api

enum class WindowMode(val id: Int) {

    FIXED(1),
    RESIZABLE(2);

    companion object {

        @JvmField val LOOKUP = values().associateBy { it.id }

        @JvmStatic
        fun of(id: Int): WindowMode {
            return LOOKUP.getValue(id)
        }
    }
}
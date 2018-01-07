package org.runestar.client.game.api

enum class WindowMode(val id: Int) {

    FIXED(1),
    RESIZABLE(2);

    companion object {

        @JvmField val VALUES = values().asList()

        @JvmStatic
        fun of(id: Int): WindowMode {
            return VALUES[id - 1]
        }
    }
}
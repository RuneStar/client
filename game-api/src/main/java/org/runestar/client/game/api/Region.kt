package org.runestar.client.game.api

inline class Region(val id: Int) {

    val x: Int get() = id shr 8

    val y: Int get() = id and 0xFF

    companion object {

        const val SIZE = 64

        fun of(x: Int, y: Int) = Region((x shl 8) or y)
    }
}
package org.runestar.client.game.api

inline class Region(val id: Int) {

    constructor(x: Int, y: Int) : this((x shl 8) or y)

    val x: Int get() = id shr 8

    val y: Int get() = id and 0xFF

    companion object {

        const val SIZE = 64
    }
}
package org.runestar.client.game.api

data class Region(val x: Int, val y: Int) {

    init {
        require(x in 0..0xFF && y in 0..0xFF)
    }

    constructor(id: Int) : this(id shr 8, id and 0xFF)

    val id get() = x shl 8 or y

    companion object {
        const val SIZE = 64
    }
}
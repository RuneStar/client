package org.runestar.client.game.api

data class WidgetId(
        val group: Int,
        val index: Int
) {

    constructor(packed: Int) : this(getGroup(packed), getIndex(packed))

    val packed: Int get() = (group shl 16) or index

    companion object {

        fun getGroup(packed: Int): Int = packed shr 16

        fun getIndex(packed: Int): Int = packed and 0xFFFF
    }
}
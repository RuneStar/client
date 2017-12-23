package org.runestar.client.game.api

data class WidgetParentId(
        val group: Int,
        val parent: Int
) {

    constructor(packed: Int) : this(packed shr 16, packed and 0xFFFF)
}
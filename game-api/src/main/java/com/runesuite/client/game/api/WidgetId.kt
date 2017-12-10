package com.runesuite.client.game.api

data class WidgetId(
        val group: Int,
        val parent: Int
) {

    constructor(packed: Int) : this(packed shr 16, packed and 0xFFFF)
}
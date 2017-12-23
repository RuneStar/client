package org.runestar.client.game.api

import java.awt.Rectangle

data class WidgetItem(
        val slot: Int,
        val id: Int,
        val quantity: Int,
        val shape: Rectangle
)
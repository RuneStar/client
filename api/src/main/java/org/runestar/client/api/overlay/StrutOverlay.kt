package org.runestar.client.api.overlay

import java.awt.Dimension
import java.awt.Graphics2D

class StrutOverlay(
        val width: Int,
        val height: Int
) : Overlay {

    override fun draw(g: Graphics2D, size: Dimension) {}

    override fun getSize(g: Graphics2D, result: Dimension) = result.setSize(width, height)
}
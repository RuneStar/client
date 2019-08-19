package org.runestar.client.api.overlay

import java.awt.Dimension
import java.awt.Graphics2D

interface Overlay {

    fun draw(g: Graphics2D, size: Dimension)

    fun getSize(g: Graphics2D, result: Dimension)
}
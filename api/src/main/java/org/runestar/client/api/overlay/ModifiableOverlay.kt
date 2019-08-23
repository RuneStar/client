package org.runestar.client.api.overlay

import java.awt.Dimension
import java.awt.Graphics2D

abstract class ModifiableOverlay : Overlay {

    var modified = true

    private var width = -1

    private var height = -1

    final override fun getSize(g: Graphics2D, result: Dimension) {
        if (!modified) return result.setSize(width, height)
        modified = false
        getSize0(g, result)
        width = result.width
        height = result.height
    }

    abstract fun getSize0(g: Graphics2D, result: Dimension)
}
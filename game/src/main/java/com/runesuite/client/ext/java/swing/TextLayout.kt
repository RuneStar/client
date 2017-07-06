package com.runesuite.client.ext.java.swing

import java.awt.Graphics2D
import java.awt.font.TextLayout

fun TextLayout(string: String, graphics2D: Graphics2D): TextLayout {
    return TextLayout(string, graphics2D.font, graphics2D.fontRenderContext)
}
package com.runesuite.client.ext.java.swing

import java.awt.Graphics2D
import java.awt.font.TextLayout
import java.awt.geom.Point2D

fun Graphics2D.create2D(): Graphics2D = create() as Graphics2D

fun Graphics2D.drawTextLayout(textLayout: TextLayout, point: Point2D) {
    textLayout.draw(this, point.x.toFloat(), point.y.toFloat())
}
package com.runesuite.client.ext.java.swing

import java.awt.Graphics
import java.awt.Image
import java.awt.Point
import java.awt.Rectangle
import javax.swing.plaf.basic.BasicGraphicsUtils

fun Graphics.drawDashedRect(rectangle: Rectangle) {
    BasicGraphicsUtils.drawDashedRect(this, rectangle.x, rectangle.y, rectangle.width, rectangle.height)
}

fun Graphics.drawStringUnderlineCharAt(text: String, index: Int, point: Point) {
    BasicGraphicsUtils.drawStringUnderlineCharAt(this, text, index, point.x, point.y)
}

fun Graphics.drawImage(img: Image) {
    drawImage(img, 0, 0, null)
}

fun Graphics.drawImage(img: Image, point: Point) {
    drawImage(img, point.x, point.y, null)
}

fun Graphics.drawPoint(point: Point) {
    point.run { drawLine(x, y, x, y) }
}

fun Graphics.drawString(string: String, point: Point) {
    drawString(string, point.x, point.y)
}
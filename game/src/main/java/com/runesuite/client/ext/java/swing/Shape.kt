package com.runesuite.client.ext.java.swing

import java.awt.Shape
import java.awt.geom.Area

fun Shape.toArea() = Area(this)

fun Iterable<Shape>.union(): Area {
    return fold(Area()) { acc, s -> acc.apply { add(s.toArea()) } }
}
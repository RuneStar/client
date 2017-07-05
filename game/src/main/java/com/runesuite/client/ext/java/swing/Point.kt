package com.runesuite.client.ext.java.swing

import java.awt.Point
import java.awt.Polygon

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)

operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)

fun List<Point>.toPolygon(): Polygon {
    return Polygon().apply {
        npoints = size
        xpoints = IntArray(size) { get(it).x }
        ypoints = IntArray(size) { get(it).y }
    }
}

fun Point.copyOf(): Point {
    return location
}
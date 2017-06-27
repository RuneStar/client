package com.runesuite.client.ext.swing

import java.awt.Shape
import java.awt.geom.Area

fun Shape.toArea() = Area(this)
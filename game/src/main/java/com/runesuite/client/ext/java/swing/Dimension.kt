package com.runesuite.client.ext.java.swing

import java.awt.Dimension

operator fun Dimension.plus(other: Dimension) = Dimension(width + other.width, height + other.height)

operator fun Dimension.minus(other: Dimension) = Dimension(width - other.width, height - other.height)
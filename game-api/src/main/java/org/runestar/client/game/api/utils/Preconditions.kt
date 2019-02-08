package org.runestar.client.game.api.utils

fun requireIn(value: Double, min: Double, max: Double) {
    if (min > max) throw IllegalArgumentException("min $min is greater than max $max")
    if (value !in min..max) throw IllegalArgumentException("value $value must be in the range $min to $max")
}

fun requireIn(value: Int, min: Int, max: Int) {
    if (min > max) throw IllegalArgumentException("min $min is greater than max $max")
    if (value !in min..max) throw IllegalArgumentException("value $value must be in the range $min to $max")
}

fun requirePercentage(value: Double) = requireIn(value, 0.0, 1.0)
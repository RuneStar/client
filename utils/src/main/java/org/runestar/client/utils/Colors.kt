@file:JvmName("Colors")

package org.runestar.client.utils

import java.awt.Color
import kotlin.math.roundToInt

/**
 * Red (0.0) <-> Yellow (0.5) <-> Green (1.0)
 *
 * @param[p] value in the range (0.0 - 1.0)
 */
fun colorForPercent(p: Double): Color {
    return when {
        p >= 1.0 -> Color.GREEN
        p <= 0.0 -> Color.RED
        p >= 0.5 -> Color(scale(p, 1.0, 0.5), 255, 0)
        else -> Color(255, scale(p, 0.0, 0.5), 0)
    }
}

private fun scale(valueIn: Double, baseMin: Double, baseMax: Double): Int {
    return (255 * (valueIn - baseMin) / (baseMax - baseMin)).roundToInt()
}
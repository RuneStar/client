package org.runestar.client.api.game

import org.runestar.client.api.util.requireIn
import org.runestar.client.api.util.requirePercentage
import kotlin.math.roundToInt

inline class RgbColor(val packed: Int) {

    val red: Int get() = (packed ushr 16) and MAX

    val redPercent: Double get() = red.toDouble() / MAX

    val green: Int get() = (packed ushr 8) and MAX

    val greenPercent: Double get() = green.toDouble() / MAX

    val blue: Int get() = packed and MAX

    val bluePercent: Double get() = blue.toDouble() / MAX

    override fun toString(): String {
        return "RgbColor($red, $green, $blue)"
    }

    companion object {

        const val MAX = 0xFF

        fun of(red: Int, green: Int, blue: Int): RgbColor {
            requireIn(red, 0, MAX)
            requireIn(green, 0, MAX)
            requireIn(blue, 0, MAX)
            return RgbColor((red shl 16) or (green shl 8) or (blue))
        }

        fun of(red: Double, green: Double, blue: Double): RgbColor {
            requirePercentage(red)
            requirePercentage(green)
            requirePercentage(blue)
            return of(
                    (red * MAX).roundToInt(),
                    (green * MAX).roundToInt(),
                    (blue * MAX).roundToInt()
            )
        }
    }
}
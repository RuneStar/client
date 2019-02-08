package org.runestar.client.game.api

import org.runestar.client.game.api.utils.requireIn
import org.runestar.client.game.api.utils.requirePercentage
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

inline class HslColor(val packed: Short) {

    val hue: Int get() = (packed.toInt() ushr 10) and HUE_MAX

    val huePercent: Double get() = hue.toDouble() / HUE_MODULUS

    val saturation: Int get() = (packed.toInt() ushr 7) and SATURATION_MAX

    val saturationPercent: Double get() = saturation.toDouble() / SATURATION_MAX

    val lightness: Int get() = packed.toInt() and LIGHTNESS_MAX

    val lightnessPercent: Double get() = lightness.toDouble() / LIGHTNESS_MAX

    override fun toString(): String {
        return "HslColor($hue/$HUE_MODULUS, $saturation/$SATURATION_MAX, $lightness/$LIGHTNESS_MAX)"
    }

    companion object {

        const val HUE_MAX = 0x3F

        const val HUE_MODULUS = HUE_MAX + 1

        const val SATURATION_MAX = 0x7

        const val LIGHTNESS_MAX = 0x7F

        fun of(hue: Int, saturation: Int, lightness: Int): HslColor {
            requireIn(hue, 0, HUE_MAX)
            requireIn(saturation, 0, SATURATION_MAX)
            requireIn(lightness, 0, LIGHTNESS_MAX)
            return HslColor(((hue shl 10) or (saturation shl 7) or (lightness)).toShort())
        }

        fun of(hue: Double, saturation: Double, lightness: Double): HslColor {
            requirePercentage(hue)
            requirePercentage(saturation)
            requirePercentage(lightness)
            return of(
                    (HUE_MODULUS * hue).roundToInt() % HUE_MODULUS,
                    (SATURATION_MAX * saturation).roundToInt(),
                    (LIGHTNESS_MAX * lightness).roundToInt()
            )
        }

        fun fromRgb(red: Double, green: Double, blue: Double): HslColor {
            requirePercentage(red)
            requirePercentage(green)
            requirePercentage(blue)
            val min = min(red, min(green, blue))
            val max = max(red, max(green, blue))
            var hue = 0.0
            var saturation = 0.0
            val lightness = (min + max) / 2.0
            val chroma = max - min
            if (chroma != 0.0) {
                saturation = chroma / (if (lightness < 0.5) (min + max) else (2.0 - chroma))
                hue = when (max) {
                    red -> ((green - blue) / chroma + 6.0) % 6.0
                    green -> (blue - red) / chroma + 2.0
                    else -> (red - green) / chroma + 4.0
                } / 6.0
            }
            return of(hue, saturation, lightness)
        }
    }
}
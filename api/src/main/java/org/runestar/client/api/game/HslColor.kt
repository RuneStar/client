package org.runestar.client.api.game

import org.runestar.client.api.util.requireIn
import org.runestar.client.api.util.requirePercentage
import org.runestar.client.api.util.rgbToHsl
import kotlin.math.roundToInt

inline class HslColor(val packed: Short) {

    val hue: Int get() = (packed.toInt() ushr 10) and HUE_MAX

    val huePercent: Double get() = (hue + 0.5) / (HUE_MAX + 1.0)

    val saturation: Int get() = (packed.toInt() ushr 7) and SATURATION_MAX

    val saturationPercent: Double get() = (saturation + 0.5) / (SATURATION_MAX + 1.0)

    val lightness: Int get() = packed.toInt() and LIGHTNESS_MAX

    val lightnessPercent: Double get() = lightness / (LIGHTNESS_MAX + 1.0)

    override fun toString(): String {
        return "HslColor($hue, $saturation, $lightness)"
    }

    companion object {

        const val HUE_MAX = 0x3F

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
                    (hue * (HUE_MAX + 1.0)).toInt().coerceAtMost(HUE_MAX),
                    (saturation * (SATURATION_MAX + 1.0)).toInt().coerceAtMost(SATURATION_MAX),
                    (lightness * (LIGHTNESS_MAX + 1.0)).roundToInt().coerceAtMost(LIGHTNESS_MAX)
            )
        }

        fun fromRgb(red: Double, green: Double, blue: Double): HslColor {
            rgbToHsl(red, green, blue) { h, s, l ->
                return of(h, s, l)
            }
        }
    }
}
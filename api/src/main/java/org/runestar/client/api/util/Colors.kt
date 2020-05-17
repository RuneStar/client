package org.runestar.client.api.util

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

inline fun rgbToHsl(
        red: Double,
        green: Double,
        blue: Double,
        hsl: (hue: Double, saturation: Double, lightness: Double) -> Unit
) {
    contract { callsInPlace(hsl, InvocationKind.EXACTLY_ONCE) }

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
        saturation = (chroma / (1.0 - abs(min + max - 1.0))).coerceAtMost(1.0)
        hue = when (max) {
            red -> ((green - blue) / chroma + 6.0) % 6.0
            green -> (blue - red) / chroma + 2.0
            else -> (red - green) / chroma + 4.0
        } / 6.0
    }
    hsl(hue, saturation, lightness)
}

inline fun hslToRgb(
        hue: Double,
        saturation: Double,
        lightness: Double,
        rgb: (red: Double, green: Double, blue: Double) -> Unit
) {
    contract { callsInPlace(rgb, InvocationKind.EXACTLY_ONCE) }

    requirePercentage(hue)
    requirePercentage(saturation)
    requirePercentage(lightness)

    var red = 0.0
    var green = 0.0
    var blue = 0.0
    val chroma = saturation * (1.0 - abs(2.0 * lightness - 1.0))
    val x = chroma * (1.0 - abs(hue * 6.0 % 2.0 - 1.0))
    when {
        hue <= (1.0 / 6.0) -> {
            red = chroma
            green = x
        }
        hue <= (2.0 / 6.0) -> {
            red = x
            green = chroma
        }
        hue <= (3.0 / 6.0) -> {
            green = chroma
            blue = x
        }
        hue <= (4.0 / 6.0) -> {
            green = x
            blue = chroma
        }
        hue <= (5.0 / 6.0) -> {
            red = x
            blue = chroma
        }
        else -> {
            red = chroma
            blue = x
        }
    }
    val min = (lightness - chroma / 2.0).coerceAtLeast(0.0)
    rgb(red + min, green + min, blue + min)
}

/*
inline fun hslToRgb(
        hue: Double,
        saturation: Double,
        lightness: Double,
        rgb: (red: Double, green: Double, blue: Double) -> Unit
) {
    contract { callsInPlace(rgb, InvocationKind.EXACTLY_ONCE) }

    requirePercentage(hue)
    requirePercentage(saturation)
    requirePercentage(lightness)

    var red = lightness
    var green = lightness
    var blue = lightness
    if (saturation != 0.0) {
        val max = if (lightness < 0.5) {
            lightness * (1.0 + saturation)
        } else {
            lightness + saturation - lightness * saturation
        }
        val min = 2.0 * lightness - max
        val chroma = max - min

        val hueA = (hue + (1.0 / 3.0)) % 1.0
        val hueB = (hue + (2.0 / 3.0)) % 1.0

        red = when {
            hueA < (1.0 / 6.0) -> min + chroma * 6.0 * hueA
            hueA < (3.0 / 6.0) -> max
            hueA < (4.0 / 6.0) -> min + chroma * ((2.0 / 3.0) - hueA) * 6.0
            else -> min
        }

        green = when {
            hue < (1.0 / 6.0) -> min + chroma * 6.0 * hue
            hue < (3.0 / 6.0) -> max
            hue < (4.0 / 6.0) -> min + chroma * ((2.0 / 3.0) - hue) * 6.0
            else -> min
        }

        blue = when {
            hueB < (1.0 / 6.0) -> min + chroma * 6.0 * hueB
            hueB < (3.0 / 6.0) -> max
            hueB < (4.0 / 6.0) -> min + chroma * ((2.0 / 3.0) - hueB) * 6.0
            else -> min
        }
    }
    rgb(red, green, blue)
}
*/
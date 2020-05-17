package org.runestar.client.api.game

import org.runestar.client.raw.CLIENT

inline class Angle(val value: Int) {

    val degrees get() = value * DEGREES_INTERVAL

    val radians get() = value * RADIANS_INTERVAL

    operator fun plus(other: Angle) = of(value + other.value)

    operator fun minus(other: Angle) = of(value - other.value)

    val sin get() = Math.sin(radians)

    val cos get() = Math.cos(radians)

    val sinInternal get() = CLIENT.rasterizer3D_sine[value]

    val cosInternal get() = CLIENT.rasterizer3D_cosine[value]

    companion object {

        const val MAX_VALUE = 2048

        private const val DEGREES_INTERVAL = 360.0 / MAX_VALUE

        private val RADIANS_INTERVAL = Math.toRadians(DEGREES_INTERVAL)

        fun of(value: Int) = Angle(value % MAX_VALUE)

        val ZERO = of(0)
    }
}
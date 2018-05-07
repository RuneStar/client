package org.runestar.client.game.api

class Angle private constructor(val value: Int) {

    val degrees get() = value * DEGREES_INTERVAL

    val radians get() = value * RADIANS_INTERVAL

    operator fun plus(other: Angle): Angle {
        return of(value + other.value)
    }

    operator fun minus(other: Angle): Angle {
        return of(value - other.value)
    }

    internal val sinInternal = (sin * 65536.0).toInt()

    internal val cosInternal = (cos * 65536.0).toInt()

    val sin get() = Math.sin(radians)

    val cos get() = Math.cos(radians)

    companion object {

        const val MAX_VALUE = 2048

        private const val DEGREES_INTERVAL = 360.0 / MAX_VALUE

        private val RADIANS_INTERVAL = Math.toRadians(DEGREES_INTERVAL)

        private val VALUES = Array(MAX_VALUE) { Angle(it) }

        fun of(value: Int): Angle {
            return VALUES[value % MAX_VALUE]
        }

        val ZERO = of(0)
    }

    override fun toString(): String {
        return "Angle($value/$MAX_VALUE)"
    }
}
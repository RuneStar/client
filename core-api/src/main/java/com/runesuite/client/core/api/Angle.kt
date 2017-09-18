package com.runesuite.client.core.api

class Angle(value: Int) {

    val value: Int

    init {
        this.value = value % MAX_VALUE
    }

    val degrees get() = value * DEGREES_INTERVAL

    val radians get() = value * RADIANS_INTERVAL

    operator fun plus(other: Angle): Angle {
        return Angle(value + other.value)
    }

    operator fun minus(other: Angle): Angle {
        return Angle(value - other.value)
    }

    internal val sinInternal get() = (sin * 65536.0).toInt()

    internal val cosInternal get() = (cos * 65536.0).toInt()

    val sin get() = Math.sin(radians)

    val cos get() = Math.cos(radians)

    companion object {
        const val MAX_VALUE = 2048
        private const val DEGREES_INTERVAL = 360.0 / MAX_VALUE
        private val RADIANS_INTERVAL = Math.toRadians(DEGREES_INTERVAL)
    }

    override fun toString(): String {
        return "Angle($value)"
    }

    override fun equals(other: Any?): Boolean {
        return other is Angle && value == other.value
    }

    override fun hashCode(): Int {
        return value
    }
}
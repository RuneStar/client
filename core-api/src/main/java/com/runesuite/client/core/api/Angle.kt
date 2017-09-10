package com.runesuite.client.core.api

class Angle(value: Int) {

    val value: Int

    init {
        this.value = value % com.runesuite.client.core.api.Angle.Companion.MAX_VALUE
    }

    val degrees get() = value * com.runesuite.client.core.api.Angle.Companion.DEGREES_INTERVAL

    val radians get() = value * com.runesuite.client.core.api.Angle.Companion.RADIANS_INTERVAL

    operator fun plus(other: com.runesuite.client.core.api.Angle): com.runesuite.client.core.api.Angle {
        return com.runesuite.client.core.api.Angle(value + other.value)
    }

    operator fun minus(other: com.runesuite.client.core.api.Angle): com.runesuite.client.core.api.Angle {
        return com.runesuite.client.core.api.Angle(value - other.value)
    }

    internal val sinInternal get() = (sin * 65536.0).toInt()

    internal val cosInternal get() = (cos * 65536.0).toInt()

    val sin get() = Math.sin(radians)

    val cos get() = Math.cos(radians)

    companion object {
        const val MAX_VALUE = 2048
        private const val DEGREES_INTERVAL = 360.0 / com.runesuite.client.core.api.Angle.Companion.MAX_VALUE
        private val RADIANS_INTERVAL = Math.toRadians(com.runesuite.client.core.api.Angle.Companion.DEGREES_INTERVAL)
    }

    override fun toString(): String {
        return "Angle($value)"
    }

    override fun equals(other: Any?): Boolean {
        return other is com.runesuite.client.core.api.Angle && value == other.value
    }

    override fun hashCode(): Int {
        return value
    }
}
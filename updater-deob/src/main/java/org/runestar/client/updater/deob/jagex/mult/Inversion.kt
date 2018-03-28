package org.runestar.client.updater.deob.jagex.mult

import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.sqrt

internal val INT_INV_MOD = BigInteger.ONE.shiftLeft(Integer.SIZE)
internal val LONG_INV_MOD = BigInteger.ONE.shiftLeft(java.lang.Long.SIZE)

internal fun invert(n: Number): Number {
    return when (n) {
        is Int -> BigInteger.valueOf(n.toLong()).modInverse(INT_INV_MOD).toInt()
        is Long -> BigInteger.valueOf(n).modInverse(LONG_INV_MOD).toLong()
        else -> error(n)
    }
}

internal fun isInvertible(n: Number): Boolean {
    return when(n) {
        is Int -> BigInteger.valueOf(n.toLong()).gcd(INT_INV_MOD) == BigInteger.ONE
        is Long -> BigInteger.valueOf(n).gcd(LONG_INV_MOD) == BigInteger.ONE
        else -> error(n)
    }
}

internal fun unfold1(n: Number): Number? {
    val nLong = n.toLong()
    for (i in 1 until sqrt(abs(nLong).toDouble()).toInt()) {
        if (nLong % i == 0L) {
            val unfolded = nLong / i
            when (n) {
                is Int -> unfolded.toInt().let { if (isInvertible(it)) return it }
                is Long -> if (isInvertible(unfolded)) return unfolded
            }
        }
    }
    return null
}
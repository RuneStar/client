package org.runestar.client.updater.deob.rs.mult

import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.sqrt

private val UNSIGNED_INT_MODULUS = BigInteger.ONE.shiftLeft(Integer.SIZE)

private val UNSIGNED_LONG_MODULUS = BigInteger.ONE.shiftLeft(java.lang.Long.SIZE)

internal fun invert(n: Number): Number {
    return when (n) {
        is Int -> BigInteger.valueOf(n.toLong()).modInverse(UNSIGNED_INT_MODULUS).toInt()
        is Long -> BigInteger.valueOf(n).modInverse(UNSIGNED_LONG_MODULUS).toLong()
        else -> error(n)
    }
}

internal fun isMultiplier(n: Number): Boolean {
    return isInvertible(n) && invert(n) != n
}

private fun isInvertible(n: Number): Boolean {
    return when (n) {
        is Int -> BigInteger.valueOf(n.toLong()).gcd(UNSIGNED_INT_MODULUS) == BigInteger.ONE
        is Long -> BigInteger.valueOf(n).gcd(UNSIGNED_LONG_MODULUS) == BigInteger.ONE
        else -> error(n)
    }
}

/**
 * n = b * m
 * where b and m are unknown, and m is invertible
 * returns m or null if it does not exist
 */
internal fun unfold(n: Number): Number? {
    return when (n) {
        is Int -> {
            for (b in 1 until sqrt(abs(n).toDouble()).toInt()) {
                if (n % b == 0) {
                    val m = n / b
                    if (isInvertible(m)) return m
                    if (isInvertible(-m)) return -m
                }
            }
            null
        }
        is Long -> {
            for (b in 1 until sqrt(abs(n).toDouble()).toInt()) {
                if (n % b == 0L) {
                    val m = n / b
                    if (isInvertible(m)) return m
                    if (isInvertible(-m)) return -m
                }
            }
            null
        }
        else -> error(n)
    }
}
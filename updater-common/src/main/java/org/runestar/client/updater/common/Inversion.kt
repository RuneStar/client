package org.runestar.client.updater.common

import java.math.BigInteger

private val UNSIGNED_INT_MODULUS = BigInteger.ONE.shiftLeft(Integer.SIZE)

private val UNSIGNED_LONG_MODULUS = BigInteger.ONE.shiftLeft(java.lang.Long.SIZE)

fun invert(n: Number): Number {
    return when (n) {
        is Int -> BigInteger.valueOf(n.toLong()).modInverse(UNSIGNED_INT_MODULUS).toInt()
        is Long -> BigInteger.valueOf(n).modInverse(UNSIGNED_LONG_MODULUS).toLong()
        else -> error(n)
    }
}

fun isInvertible(n: Number): Boolean {
    return when (n) {
        is Int -> BigInteger.valueOf(n.toLong()).gcd(UNSIGNED_INT_MODULUS) == BigInteger.ONE
        is Long -> BigInteger.valueOf(n).gcd(UNSIGNED_LONG_MODULUS) == BigInteger.ONE
        else -> error(n)
    }
}
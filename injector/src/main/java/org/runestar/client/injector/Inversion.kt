package org.runestar.client.injector

import java.math.BigInteger

private val INT_INV_MOD = BigInteger.ONE.shiftLeft(Integer.SIZE)

private val LONG_INV_MOD = BigInteger.ONE.shiftLeft(java.lang.Long.SIZE)

internal fun invert(n: Number?): Number? = when (n) {
    is Int -> BigInteger.valueOf(n.toLong()).modInverse(INT_INV_MOD).toInt()
    is Long -> BigInteger.valueOf(n).modInverse(LONG_INV_MOD).toLong()
    else -> null
}
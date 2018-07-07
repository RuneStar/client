package org.runestar.client.updater.deob.rs.mult

import org.runestar.client.updater.common.invert
import org.runestar.client.updater.common.isInvertible
import kotlin.math.abs
import kotlin.math.sqrt

internal fun isMultiplier(n: Number): Boolean {
    return isInvertible(n) && invert(n) != n
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
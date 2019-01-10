package org.runestar.client.common

fun requirePositive(n: Int) {
    if (n <= 0) throw IllegalArgumentException("Argument was $n but must be positive")
}

fun requireNotNegative(n: Int) {
    if (n < 0) throw IllegalArgumentException("Argument was $n but must not be negative")
}
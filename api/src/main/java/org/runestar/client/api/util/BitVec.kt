package org.runestar.client.api.util

inline class BitVec(private val n: Int) {

    operator fun get(index: Int): Boolean = (n and (1 shl index)) != 0

    val size: Int get() = Integer.bitCount(n)

    fun isEmpty(): Boolean = n == 0
}
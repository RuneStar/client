package org.runestar.client.common

import kotlin.experimental.xor
import kotlin.math.min

fun ByteArray.xorAssign(other: ByteArray) {
    repeat(min(size, other.size)) { i ->
        this[i] = this[i] xor other[i]
    }
}
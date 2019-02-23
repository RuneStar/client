package org.runestar.client.common

import java.lang.invoke.MethodHandles
import kotlin.experimental.xor
import kotlin.math.min

fun ByteArray.xorAssign(other: ByteArray) {
    repeat(min(size, other.size)) { i ->
        this[i] = this[i] xor other[i]
    }
}

inline val lookupClass: Class<*> get() = MethodHandles.lookup().lookupClass()

inline val lookupClassLoader: ClassLoader get() = lookupClass.classLoader
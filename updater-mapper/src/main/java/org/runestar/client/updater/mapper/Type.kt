package org.runestar.client.updater.mapper

import org.objectweb.asm.Type

val Type.arrayDimensions: Int get() {
    check(sort != Type.METHOD)
    if (sort == Type.ARRAY) return dimensions
    return 0
}

val Type.isPrimitive: Boolean get() {
    check(sort != Type.METHOD)
    return when (sort) {
        Type.ARRAY -> false
        Type.OBJECT -> false
        else -> true
    }
}

val Type.baseType: Type get() {
    check(sort != Type.METHOD)
    if (arrayDimensions == 0) return this
    return elementType
}

fun Type.withDimensions(dimensions: Int): Type {
    check(sort != Type.METHOD)
    check(dimensions >= 0)
    val prefix = String(CharArray(dimensions) { '[' })
    return Type.getType(prefix + baseType.descriptor)
}
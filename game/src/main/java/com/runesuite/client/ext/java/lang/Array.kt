package com.runesuite.client.ext.java.lang

/**
 * Deep copies component arrays, shallow copies everything else.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Array<T>.deepCopyOf(): Array<T> {
    if (size == 0) return this
    val componentType: Class<*> = this::class.java.componentType
    return if (componentType.isArray) {
        val newArray = java.lang.reflect.Array.newInstance(componentType, size) as Array<T>
        val componentComponentType = componentType.componentType
        if (componentComponentType.isPrimitive) {
            for (i in indices) {
                val elem = get(i)
                val length = java.lang.reflect.Array.getLength(elem)
                newArray[i] = java.lang.reflect.Array.newInstance(componentComponentType, length) as T
                System.arraycopy(elem, 0, newArray[i], 0, length)
            }
        } else {
            for (i in indices) {
                val elem = get(i) as Array<*>
                newArray[i] = elem.deepCopyOf() as T
            }
        }
        newArray
    } else {
        clone()
    }
}

inline fun <T, reified R> Array<T>.arrayMap(transform: (T) -> R): Array<R> {
    return Array(size) {
        transform(get(it))
    }
}
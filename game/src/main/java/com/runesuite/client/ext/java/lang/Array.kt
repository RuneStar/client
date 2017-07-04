package com.runesuite.client.ext.java.lang

@Suppress("UNCHECKED_CAST")
fun <T> Array<T>.deepCopyOf(): Array<T> {
    val componentType: Class<*> = this::class.java.componentType
    return if (componentType.isArray) {
        val newArray = java.lang.reflect.Array.newInstance(componentType, size) as Array<T>
        val componentComponentType = componentType.componentType
        if (componentComponentType.isPrimitive) {
            for (i in indices) {
                newArray[i] = java.lang.reflect.Array.newInstance(componentComponentType, size) as T
                System.arraycopy(get(i), 0, newArray[i], 0, java.lang.reflect.Array.getLength(newArray[i]))
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
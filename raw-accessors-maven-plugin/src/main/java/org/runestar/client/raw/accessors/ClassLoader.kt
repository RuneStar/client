package org.runestar.client.raw.accessors

import java.lang.reflect.Array

/**
 * Loads a class by its descriptor in accordance with
 * [JVMS 4.3](https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.3).
 *
 * Primitive types are resolved using their [Class] instances such as [Integer.TYPE]. Object types are loaded by
 * delegating to [ClassLoader.loadClass].
 *
 * @return the [Class] represented by [descriptor] as loaded by this [ClassLoader]
 *
 * @throws[IllegalArgumentException] if [descriptor] is not valid
 * @throws[ClassNotFoundException] if the class was not found
 */
fun ClassLoader.loadClassFromDescriptor(descriptor: CharSequence): Class<*> {
    return when (descriptor.length) {
        0 -> throw invalidDescriptorArgument(descriptor)
        1 -> {
            when (descriptor[0]) {
                'V' -> java.lang.Void.TYPE
                'I' -> java.lang.Integer.TYPE
                'J' -> java.lang.Long.TYPE
                'F' -> java.lang.Float.TYPE
                'D' -> java.lang.Double.TYPE
                'S' -> java.lang.Short.TYPE
                'B' -> java.lang.Byte.TYPE
                'C' -> java.lang.Character.TYPE
                'Z' -> java.lang.Boolean.TYPE
                else -> throw invalidDescriptorArgument(descriptor)
            }
        }
        else -> {
            when (descriptor[0]) {
                'L' -> {
                    if (descriptor.endsWith(';')) {
                        loadClass(descriptor.substring(1, descriptor.length - 1).replace('/', '.'))
                    } else {
                        throw invalidDescriptorArgument(descriptor)
                    }
                }
                '[' -> {
                    val componentType = loadClassFromDescriptor(descriptor.subSequence(1, descriptor.length))
                    Array.newInstance(componentType, 0).javaClass
                }
                else -> throw invalidDescriptorArgument(descriptor)
            }
        }
    }
}

private fun invalidDescriptorArgument(descriptor: CharSequence): IllegalArgumentException {
    return IllegalArgumentException("'$descriptor' is not a valid descriptor")
}
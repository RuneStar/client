package org.runestar.client.common

/**
 * Returns a view of this [List] containing the first [n] elements
 *
 * @throw[IllegalArgumentException] if [n] is less than zero
 *
 * @see[List.subList]
 * @see[take]
 */
fun <T> List<T>.subListTake(n: Int): List<T> {
    requireNotNegative(n)
    if (n >= size) return this
    return subList(0, n)
}

@JvmName("subListTakeMutable")
fun <T> MutableList<T>.subListTake(n: Int): MutableList<T> {
    return (this as List<T>).subListTake(n) as MutableList<T>
}

/**
 * Returns a view of this [List] without the last [n] elements
 *
 * @throw[IllegalArgumentException] if [n] is less than zero
 *
 * @see[List.subList]
 * @see[dropLast]
 */
fun <T> List<T>.subListDropLast(n: Int): List<T> {
    requireNotNegative(n)
    return subListTake((size - n).coerceAtLeast(0))
}

@JvmName("subListDropLastMutable")
fun <T> MutableList<T>.subListDropLast(n: Int): MutableList<T> {
    return (this as List<T>).subListDropLast(n) as MutableList<T>
}

/**
 * Returns a view of this [List] containing the last [n] elements
 *
 * @throw[IllegalArgumentException] if [n] is less than zero
 *
 * @see[List.subList]
 * @see[takeLast]
 */
fun <T> List<T>.subListTakeLast(n: Int): List<T> {
    requireNotNegative(n)
    if (n >= size) return this
    return subList(size - n, size)
}

@JvmName("subListTakeLastMutable")
fun <T> MutableList<T>.subListTakeLast(n: Int): MutableList<T> {
    return (this as List<T>).subListTakeLast(n) as MutableList<T>
}

/**
 * Returns a view of this [List] without the first [n] elements
 *
 * @throw[IllegalArgumentException] if [n] is less than zero
 *
 * @see[List.subList]
 * @see[drop]
 */
fun <T> List<T>.subListDrop(n: Int): List<T> {
    requireNotNegative(n)
    return subListTakeLast((size - n).coerceAtLeast(0))
}

@JvmName("subListDropMutable")
fun <T> MutableList<T>.subListDrop(n: Int): MutableList<T> {
    return (this as List<T>).subListDrop(n) as MutableList<T>
}

fun <T> List<T>.startsWith(elements: List<T>): Boolean {
    return subListTake(elements.size) == elements
}

fun <T> List<T>.startsWith(vararg elements: T): Boolean {
    return startsWith(elements.asList())
}

fun <T> List<T>.endsWith(elements: List<T>): Boolean {
    return subListTakeLast(elements.size) == elements
}

fun <T> List<T>.endsWith(vararg elements: T): Boolean {
    return endsWith(elements.asList())
}
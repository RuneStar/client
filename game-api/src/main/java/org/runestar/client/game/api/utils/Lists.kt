package org.runestar.client.game.api.utils

import java.util.*

fun <T> MutableList<T>.addNotNull(t: T?) {
    t?.let { add(it) }
}

fun <T> cascadingListOf(a: T?): List<T> {
    val m1 = a ?: return emptyList()
    return Collections.singletonList(m1)
}

fun <T> cascadingListOf(a: T?, b: T?): List<T> {
    val m1 = a ?: return emptyList()
    val m2 = b ?: return Collections.singletonList(m1)
    return Arrays.asList(m1, m2)
}

fun <T> cascadingListOf(a: T?, b: T?, c: T?): List<T> {
    val m1 = a ?: return emptyList()
    val m2 = b ?: return Collections.singletonList(m1)
    val m3 = c ?: return Arrays.asList(m1, m2)
    return Arrays.asList(m1, m2, m3)
}
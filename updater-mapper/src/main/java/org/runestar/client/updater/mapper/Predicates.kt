package org.runestar.client.updater.mapper

inline infix fun <T> Predicate<T>.and(crossinline other: Predicate<T>): Predicate<T> {
    return { this(it) && other(it) }
}

typealias Predicate<T> = (T) -> Boolean

fun <T> predicateOf(predicate: Predicate<T>): Predicate<T> = predicate
package com.runesuite.client.game.raw

/**
 * A class that wraps an [Accessor] value.
 */
abstract class Wrapper {

    abstract val accessor: Accessor

    override fun equals(other: Any?): Boolean {
        return other is Wrapper && accessor == other.accessor
    }

    override fun hashCode(): Int {
        return accessor.hashCode()
    }
}
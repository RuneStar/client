package com.runesuite.client.game.raw

/**
 * A class that wraps an [Accessor] value.
 *
 * Allows for the addition and redefinition of fields and methods while keeping but hiding the originals.
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
package com.runesuite.client.raw

abstract class Wrapper {

    abstract val accessor: Accessor

    override fun equals(other: Any?): Boolean {
        return other is Wrapper && accessor == other.accessor
    }

    override fun hashCode(): Int {
        return accessor.hashCode()
    }
}
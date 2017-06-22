package com.runesuite.client.base

abstract class Wrapper {

    abstract val accessor: Accessor

    override fun equals(other: Any?): Boolean {
        return other is Wrapper && accessor == other.accessor
    }

    override fun hashCode(): Int {
        return accessor.hashCode()
    }

    override fun toString(): String {
        return "Wrapper($accessor)"
    }
}
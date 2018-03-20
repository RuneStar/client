package org.runestar.client.game.api

abstract class Wrapper(open val accessor: Any) {

    final override fun hashCode(): Int {
        return System.identityHashCode(accessor)
    }

    final override fun equals(other: Any?): Boolean {
        return other is Wrapper && accessor === other.accessor
    }

    override fun toString(): String {
        return "${javaClass.simpleName}($accessor)"
    }
}
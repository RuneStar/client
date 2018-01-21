package org.runestar.client.game.api

data class Varps(
        @Suppress("ArrayInDataClass") val accessor: IntArray
) {

    operator fun get(varbit: Varbit): Int = accessor[varbit.index] ushr varbit.lowBit and varbit.max
}
package org.runestar.client.game.api

data class Varps(
        @Suppress("ArrayInDataClass") val array: IntArray
) {

    operator fun get(varbit: Varbit): Int = array[varbit.index] ushr varbit.lowBit and varbit.max
}
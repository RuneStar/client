package com.runesuite.client.game.api

data class Varbit(
        val index: Int,
        val lowBit: Int,
        val highBit: Int
) {

    init {
        require(lowBit <= highBit &&
                lowBit in 0 until Integer.SIZE &&
                highBit in 0 until Integer.SIZE &&
                index > 0)
    }

    val bitLength: Int get() = highBit - lowBit + 1

    val max: Int get() = (1 shl bitLength) - 1
}
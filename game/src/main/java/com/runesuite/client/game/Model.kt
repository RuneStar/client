package com.runesuite.client.game


import java.util.*

class Model(
        private val base: Position,
        private val orientation: Angle = Angle(0),
        private val indicesX: IntArray,
        private val indicesY: IntArray,
        private val indicesZ: IntArray,
        private val verticesX: IntArray,
        private val verticesY: IntArray,
        private val verticesZ: IntArray
) {

    init {
        rotateTo(orientation)
    }

    private fun rotateTo(orientation: Angle) {
        if (orientation.value == 0) return
        val sin = orientation.sinInternal
        val cos = orientation.cosInternal
        for (i in 0..verticesZ.size - 1) {
            val x = verticesX[i]
            val z = verticesZ[i]
            verticesX[i] = z * sin + x * cos shr 16
            verticesZ[i] = z * cos - x * sin shr 16
        }
    }

    val triangles: List<List<Position>> by lazy {
        val list = ArrayList<List<Position>>(indicesX.size)
        for (i in 0..indicesX.size - 1) {
            val p0 = base.plusLocal(verticesX[indicesX[i]], verticesZ[indicesX[i]], -1 * verticesY[indicesX[i]])
            val p1 = base.plusLocal(verticesX[indicesY[i]], verticesZ[indicesY[i]], -1 * verticesY[indicesY[i]])
            val p2 = base.plusLocal(verticesX[indicesZ[i]], verticesZ[indicesZ[i]], -1 * verticesY[indicesZ[i]])
            list.add(listOf(p0, p1, p2))
        }
        list
    }
}
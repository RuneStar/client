package com.runesuite.client.game.api

import com.hunterwb.kxtra.swing.polygon.Polygon
import com.runesuite.client.game.api.live.Projection
import com.runesuite.client.game.raw.access.XModel
import java.awt.Polygon

class Model(
        val base: com.runesuite.client.game.api.Position,
        val orientation: com.runesuite.client.game.api.Angle,
        val model: XModel
) {

    init {
        rotateTo(orientation)
    }

    private fun rotateTo(orientation: com.runesuite.client.game.api.Angle) {
        if (orientation.value == 0) return
        val sin = orientation.sinInternal
        val cos = orientation.cosInternal
        for (i in 0 until model.verticesZ.size) {
            val x = model.verticesX[i]
            val z = model.verticesZ[i]
            model.verticesX[i] = z * sin + x * cos shr 16
            model.verticesZ[i] = z * cos - x * sin shr 16
        }
    }

    fun getTriangles(): List<List<com.runesuite.client.game.api.Position>> {
        val list = ArrayList<List<com.runesuite.client.game.api.Position>>(model.indicesX.size)
        for (i in 0..model.indicesX.size - 1) {
            val p0 = base.plusLocal(model.verticesX[model.indicesX[i]], model.verticesZ[model.indicesX[i]], -1 * model.verticesY[model.indicesX[i]])
            val p1 = base.plusLocal(model.verticesX[model.indicesY[i]], model.verticesZ[model.indicesY[i]], -1 * model.verticesY[model.indicesY[i]])
            val p2 = base.plusLocal(model.verticesX[model.indicesZ[i]], model.verticesZ[model.indicesZ[i]], -1 * model.verticesY[model.indicesZ[i]])
            list.add(listOf(p0, p1, p2))
        }
        return list
    }

    fun trianglesToScreen(projection: Projection = Projection.Viewport.LIVE): List<Polygon> {
        return getTriangles().map { it.map { projection.toScreen(it, base) } }
                .map { Polygon(it) }
    }
}
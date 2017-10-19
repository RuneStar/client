package com.runesuite.client.game.api.live

import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.access.XTile

abstract class TileEntities<T> {

    abstract protected fun fromTile(sceneTile: com.runesuite.client.game.api.SceneTile, xTile: XTile?): T

    open fun getAt(sceneTile: com.runesuite.client.game.api.SceneTile): T {
        require(sceneTile.isLoaded) { sceneTile }
        val tile = Client.accessor.scene.tiles[sceneTile.plane][sceneTile.x][sceneTile.y]
        return fromTile(sceneTile, tile)
    }

    fun getOnPlane(plane: Int): List<List<T>> {
        require(plane in 0 until Scene.PLANE_SIZE) { plane }
        val objs = ArrayList<ArrayList<T>>(Scene.SIZE)
        for (x in 0 until Scene.SIZE) {
            objs.add(ArrayList(Scene.SIZE))
            for (y in 0 until Scene.SIZE) {
                val tile = com.runesuite.client.game.api.SceneTile(x, y, plane)
                objs[x].add(getAt(tile))
            }
        }
        return objs
    }

    fun get(): List<List<List<T>>> {
        val objs = ArrayList<ArrayList<ArrayList<T>>>(Scene.PLANE_SIZE)
        for (plane in 0 until Scene.PLANE_SIZE) {
            objs.add(ArrayList(Scene.SIZE))
            for (x in 0 until Scene.SIZE) {
                objs[plane].add(ArrayList(Scene.SIZE))
                for (y in 0 until Scene.SIZE) {
                    val tile = com.runesuite.client.game.api.SceneTile(x, y, plane)
                    objs[plane][x].add(getAt(tile))
                }
            }
        }
        return objs
    }

    abstract class Single<T : Any> : TileEntities<T?>() {

        fun getOnPlaneFlat(plane: Int): List<T> {
            return getOnPlane(plane).flatMap { it.mapNotNull { it } }
        }

        fun getFlat(): List<T> {
            return get().flatMap { it.flatMap { it.mapNotNull { it } } }
        }
    }

    abstract class Many<T : Any> : TileEntities<List<T>>() {

        fun getOnPlaneFlat(plane: Int): List<T> {
            return getOnPlane(plane).flatMap { it.flatMap { it } }
        }

        fun getFlat(): List<T> {
            return get().flatMap { it.flatMap { it.flatMap { it } } }
        }
    }
}
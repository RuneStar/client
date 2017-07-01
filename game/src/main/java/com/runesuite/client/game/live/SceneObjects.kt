package com.runesuite.client.game.live

import com.runesuite.client.base.Client
import com.runesuite.client.base.access.XTile
import com.runesuite.client.game.SceneObject
import com.runesuite.client.game.SceneTile

object SceneObjects {

    object Wall : Base.Single<SceneObject.Wall>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): SceneObject.Wall? {
            val obj = xTile?.wallDecoration ?: return null
            return SceneObject.Wall(obj, sceneTile)
        }
    }

    object Floor : Base.Single<SceneObject.Floor>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): SceneObject.Floor? {
            val obj = xTile?.floorDecoration ?: return null
            return SceneObject.Floor(obj, sceneTile)
        }
    }

    object Boundary : Base.Single<SceneObject.Boundary>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): SceneObject.Boundary? {
            val obj = xTile?.boundaryObject ?: return null
            return SceneObject.Boundary(obj, sceneTile)
        }
    }

    object Interactable : Base.Many<SceneObject.Interactable>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): List<SceneObject.Interactable> {
            val obj = xTile?.gameObjects ?: return emptyList()
            return obj.mapNotNull { it?.let { SceneObject.Interactable(it, sceneTile) } }
        }
    }

    abstract class Base<T> {

        abstract protected fun fromTile(sceneTile: SceneTile, xTile: XTile?): T

        open fun getAt(sceneTile: SceneTile): T {
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
                    val tile = SceneTile(x, y, plane)
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
                        val tile = SceneTile(x, y, plane)
                        objs[plane][x].add(getAt(tile))
                    }
                }
            }
            return objs
        }

        abstract class Single<T : Any> : Base<T?>() {

            fun getOnPlaneFlat(plane: Int): List<T> {
                return getOnPlane(plane).flatMap { it.mapNotNull { it } }
            }

            fun getFlat(): List<T> {
                return get().flatMap { it.flatMap { it.mapNotNull { it } } }
            }
        }

        abstract class Many<T : Any> : Base<List<T>>() {

            fun getOnPlaneFlat(plane: Int): List<T> {
                return getOnPlane(plane).flatMap { it.flatMap { it } }
            }

            fun getFlat(): List<T> {
                return get().flatMap { it.flatMap { it.flatMap { it } } }
            }
        }
    }
}
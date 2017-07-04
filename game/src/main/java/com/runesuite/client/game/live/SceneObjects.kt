package com.runesuite.client.game.live

import com.runesuite.client.base.access.XTile
import com.runesuite.client.game.SceneObject
import com.runesuite.client.game.SceneTile

object SceneObjects : TileEntities.Many<SceneObject>() {

    override fun fromTile(sceneTile: SceneTile, xTile: XTile?): List<SceneObject> {
        xTile ?: return emptyList()
        val list = ArrayList<SceneObject>()
        xTile.floorDecoration?.let { list.add(SceneObject.Floor(it, sceneTile)) }
        xTile.wallDecoration?.let { list.add(SceneObject.Wall(it, sceneTile)) }
        xTile.boundaryObject?.let { list.add(SceneObject.Boundary(it, sceneTile)) }
        xTile.gameObjects?.mapNotNullTo(list) { it?.let { SceneObject.Interactable(it, sceneTile) } }
        return list
    }

    object Wall : TileEntities.Single<SceneObject.Wall>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): SceneObject.Wall? {
            val obj = xTile?.wallDecoration ?: return null
            return SceneObject.Wall(obj, sceneTile)
        }
    }

    object Floor : TileEntities.Single<SceneObject.Floor>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): SceneObject.Floor? {
            val obj = xTile?.floorDecoration ?: return null
            return SceneObject.Floor(obj, sceneTile)
        }
    }

    object Boundary : TileEntities.Single<SceneObject.Boundary>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): SceneObject.Boundary? {
            val obj = xTile?.boundaryObject ?: return null
            return SceneObject.Boundary(obj, sceneTile)
        }
    }

    object Interactable : TileEntities.Many<SceneObject.Interactable>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): List<SceneObject.Interactable> {
            val obj = xTile?.gameObjects ?: return emptyList()
            return obj.mapNotNull { it?.let { SceneObject.Interactable(it, sceneTile) } }
        }
    }
}
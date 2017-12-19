package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.SceneObject
import com.runesuite.client.game.api.SceneTile
import com.runesuite.client.game.raw.access.XTile

object SceneObjects : TileEntities.Many<SceneObject>() {

    override fun fromTile(sceneTile: SceneTile, xTile: XTile?): List<SceneObject> {
        val list = ArrayList<SceneObject>()
        xTile?.floorDecoration?.let { list.add(SceneObject.Floor(it)) }
        xTile?.wallDecoration?.let { list.add(SceneObject.Wall(it)) }
        xTile?.boundaryObject?.let { list.add(SceneObject.Boundary(it)) }
        xTile?.gameObjects?.mapNotNullTo(list) { it?.let { SceneObject.Interactable(it) } }
        return list
    }

    object Wall : TileEntities.Single<SceneObject.Wall>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): SceneObject.Wall? {
            val obj = xTile?.wallDecoration ?: return null
            return SceneObject.Wall(obj)
        }
    }

    object Floor : TileEntities.Single<SceneObject.Floor>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): SceneObject.Floor? {
            val obj = xTile?.floorDecoration ?: return null
            return SceneObject.Floor(obj)
        }
    }

    object Boundary : TileEntities.Single<SceneObject.Boundary>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): SceneObject.Boundary? {
            val obj = xTile?.boundaryObject ?: return null
            return SceneObject.Boundary(obj)
        }
    }

    object Interactable : TileEntities.Many<SceneObject.Interactable>() {

        override fun fromTile(sceneTile: SceneTile, xTile: XTile?): List<SceneObject.Interactable> {
            val obj = xTile?.gameObjects ?: return emptyList()
            return obj.mapNotNull { it?.let { SceneObject.Interactable(it) } }
        }
    }
}
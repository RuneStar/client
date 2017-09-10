package com.runesuite.client.core.api.live

import com.runesuite.client.core.raw.Client.accessor
import com.runesuite.client.core.raw.access.XTile

object SceneObjects : TileEntities.Many<com.runesuite.client.core.api.SceneObject>() {

    override fun fromTile(sceneTile: com.runesuite.client.core.api.SceneTile, xTile: XTile?): List<com.runesuite.client.core.api.SceneObject> {
        xTile ?: return emptyList()
        val list = ArrayList<com.runesuite.client.core.api.SceneObject>()
        xTile.floorDecoration?.let { list.add(com.runesuite.client.core.api.SceneObject.Floor(it, sceneTile)) }
        xTile.wallDecoration?.let { list.add(com.runesuite.client.core.api.SceneObject.Wall(it, sceneTile)) }
        xTile.boundaryObject?.let { list.add(com.runesuite.client.core.api.SceneObject.Boundary(it, sceneTile)) }
        xTile.gameObjects?.mapNotNullTo(list) { it?.let { com.runesuite.client.core.api.SceneObject.Interactable(it, sceneTile) } }
        return list
    }

    object Wall : TileEntities.Single<com.runesuite.client.core.api.SceneObject.Wall>() {

        override fun fromTile(sceneTile: com.runesuite.client.core.api.SceneTile, xTile: XTile?): com.runesuite.client.core.api.SceneObject.Wall? {
            val obj = xTile?.wallDecoration ?: return null
            return com.runesuite.client.core.api.SceneObject.Wall(obj, sceneTile)
        }
    }

    object Floor : TileEntities.Single<com.runesuite.client.core.api.SceneObject.Floor>() {

        override fun fromTile(sceneTile: com.runesuite.client.core.api.SceneTile, xTile: XTile?): com.runesuite.client.core.api.SceneObject.Floor? {
            val obj = xTile?.floorDecoration ?: return null
            return com.runesuite.client.core.api.SceneObject.Floor(obj, sceneTile)
        }
    }

    object Boundary : TileEntities.Single<com.runesuite.client.core.api.SceneObject.Boundary>() {

        override fun fromTile(sceneTile: com.runesuite.client.core.api.SceneTile, xTile: XTile?): com.runesuite.client.core.api.SceneObject.Boundary? {
            val obj = xTile?.boundaryObject ?: return null
            return com.runesuite.client.core.api.SceneObject.Boundary(obj, sceneTile)
        }
    }

    object Interactable : TileEntities.Many<com.runesuite.client.core.api.SceneObject.Interactable>() {

        override fun fromTile(sceneTile: com.runesuite.client.core.api.SceneTile, xTile: XTile?): List<com.runesuite.client.core.api.SceneObject.Interactable> {
            val obj = xTile?.gameObjects ?: return emptyList()
            return obj.mapNotNull { it?.let { com.runesuite.client.core.api.SceneObject.Interactable(it, sceneTile) } }
        }
    }
}
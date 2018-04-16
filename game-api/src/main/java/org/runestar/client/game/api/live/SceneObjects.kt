package org.runestar.client.game.api.live

import org.runestar.client.game.api.SceneObject
import org.runestar.client.game.api.TileObjects
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XTile

object SceneObjects : TileObjects.Many<SceneObject>(Client.accessor.scene) {

    override fun fromTile(tile: XTile): Iterator<SceneObject> {
        val list = ArrayList<SceneObject>()
        tile.floorDecoration?.let { list.add(SceneObject.Floor(it, tile.plane)) }
        tile.wallDecoration?.let { list.add(SceneObject.Wall(it, tile.plane)) }
        tile.boundaryObject?.let { list.add(SceneObject.Boundary(it, tile.plane)) }
        tile.gameObjects?.let { it.mapNotNullTo(list) { it?.let { SceneObject.Interactable(it) } } }
        return list.iterator()
    }

    object Wall : TileObjects.Single<SceneObject.Wall>(Client.accessor.scene) {

        override fun fromTile(tile: XTile): SceneObject.Wall? {
            val v = tile.wallDecoration ?: return null
            return SceneObject.Wall(v, tile.plane)
        }
    }

    object Floor : TileObjects.Single<SceneObject.Floor>(Client.accessor.scene) {

        override fun fromTile(tile: XTile): SceneObject.Floor? {
            val v = tile.floorDecoration ?: return null
            return SceneObject.Floor(v, tile.plane)
        }
    }

    object Boundary : TileObjects.Single<SceneObject.Boundary>(Client.accessor.scene) {

        override fun fromTile(tile: XTile): SceneObject.Boundary? {
            val v = tile.boundaryObject ?: return null
            return SceneObject.Boundary(v, tile.plane)
        }
    }

    object Interactable : TileObjects.Many<SceneObject.Interactable>(Client.accessor.scene) {

        override fun fromTile(tile: XTile): Iterator<SceneObject.Interactable> {
            return object : AbstractIterator<SceneObject.Interactable>() {

                private var i = 0

                override fun computeNext() {
                    if (i >= tile.gameObjectsCount) return done()
                    val o = tile.gameObjects[i++] ?: return done()
                    setNext(SceneObject.Interactable(o))
                }
            }
        }
    }
}
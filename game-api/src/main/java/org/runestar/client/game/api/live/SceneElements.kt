package org.runestar.client.game.api.live

import io.reactivex.Observable
import org.runestar.client.game.api.SceneElement
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.api.TileObjects
import org.runestar.client.game.api.utils.addNotNull
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.access.XGameObject
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.game.raw.access.XTile

object SceneElements : TileObjects.Many<SceneElement>(Client.accessor.scene) {

    override fun fromTile(tile: XTile): List<SceneElement> {
        val list = ArrayList<SceneElement>()
        list.addNotNull(Floor.fromTile(tile))
        list.addNotNull(Wall.fromTile(tile))
        list.addNotNull(Boundary.fromTile(tile))
        list.addNotNull(ItemPile.fromTile(tile))
        list.addAll(Game.fromTile(tile))
        return list
    }

    val clears: Observable<Unit> = XScene.clear.exit.map { Unit }

    override val additions: Observable<SceneElement> = Observable.empty<SceneElement>()
            .mergeWith(Wall.additions)
            .mergeWith(Floor.additions)
            .mergeWith(Boundary.additions)
            .mergeWith(ItemPile.additions)
            .mergeWith(Game.additions)

    override val removals: Observable<SceneElement> = Observable.empty<SceneElement>()
            .mergeWith(Wall.removals)
            .mergeWith(Floor.removals)
            .mergeWith(Boundary.removals)
            .mergeWith(ItemPile.removals)
            .mergeWith(Game.removals)

    object Object : TileObjects.Many<SceneElement>(Client.accessor.scene) {

        override val additions: Observable<SceneElement> = Observable.empty<SceneElement>()
                .mergeWith(Wall.additions)
                .mergeWith(Floor.additions)
                .mergeWith(Boundary.additions)
                .mergeWith(Game.additions.filter(SceneElement::isObject))

        override val removals: Observable<SceneElement> = Observable.empty<SceneElement>()
                .mergeWith(Wall.removals)
                .mergeWith(Floor.removals)
                .mergeWith(Boundary.removals)
                .mergeWith(Game.removals.filter(SceneElement::isObject))

        override fun fromTile(tile: XTile): List<SceneElement> {
            val list = ArrayList<SceneElement>()
            list.addNotNull(Floor.fromTile(tile))
            list.addNotNull(Wall.fromTile(tile))
            list.addNotNull(Boundary.fromTile(tile))
            Game.fromTile(tile).filterTo(list, SceneElement::isObject)
            return list
        }
    }

    object Wall : TileObjects.Single<SceneElement.Wall>(Client.accessor.scene) {

        override val additions: Observable<SceneElement.Wall> = XScene.newWallDecoration.exit.map {
                    val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
                    SceneElement.Wall(tile.wallDecoration, tile.plane)
        }

        override val removals: Observable<SceneElement.Wall> = XScene.removeWallDecoration.enter.map {
            val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
            SceneElement.Wall(tile.wallDecoration, tile.plane)
        }

        override fun fromTile(tile: XTile): SceneElement.Wall? {
            val v = tile.wallDecoration ?: return null
            return SceneElement.Wall(v, tile.plane)
        }
    }

    object Floor : TileObjects.Single<SceneElement.Floor>(Client.accessor.scene) {

        override val additions: Observable<SceneElement.Floor> = XScene.newFloorDecoration.exit.map {
                    val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
                    SceneElement.Floor(tile.floorDecoration, tile.plane)
        }

        override val removals: Observable<SceneElement.Floor> = XScene.removeFloorDecoration.enter.map {
            val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
            SceneElement.Floor(tile.floorDecoration, tile.plane)
        }

        override fun fromTile(tile: XTile): SceneElement.Floor? {
            val v = tile.floorDecoration ?: return null
            return SceneElement.Floor(v, tile.plane)
        }
    }

    object Boundary : TileObjects.Single<SceneElement.Boundary>(Client.accessor.scene) {

        override val additions: Observable<SceneElement.Boundary> = XScene.newBoundaryObject.exit.map {
                    val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
                    SceneElement.Boundary(tile.boundaryObject, tile.plane)
        }

        override val removals: Observable<SceneElement.Boundary> = XScene.removeBoundaryObject.enter.map {
            val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
            SceneElement.Boundary(tile.boundaryObject, tile.plane)
        }

        override fun fromTile(tile: XTile): SceneElement.Boundary? {
            val v = tile.boundaryObject ?: return null
            return SceneElement.Boundary(v, tile.plane)
        }
    }

    object ItemPile : TileObjects.Single<SceneElement.ItemPile>(Client.accessor.scene) {

        override val additions: Observable<SceneElement.ItemPile> = XScene.newGroundItemPile.exit.map {
            val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
            SceneElement.ItemPile(tile.groundItemPile, tile.plane)
        }

        private val changes: Observable<SceneElement.ItemPile> = XScene.newGroundItemPile.enter
                .map { checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int)) }
                .filter { it.groundItemPile != null }
                .map { SceneElement.ItemPile(it.groundItemPile, it.plane) }

        private val removes: Observable<SceneElement.ItemPile> = XScene.removeGroundItemPile.enter
                .map { SceneTile(it.arguments[1] as Int, it.arguments[2] as Int, it.arguments[0] as Int) }
                .filter { getTile(it.plane, it.x, it.y) != null }
                .map { getTile(it.plane, it.x, it.y) }
                .filter { it.groundItemPile != null }
                .map { SceneElement.ItemPile(it.groundItemPile, it.plane) }

        override val removals: Observable<SceneElement.ItemPile> = Observable.merge(changes, removes)

        override fun fromTile(tile: XTile): SceneElement.ItemPile? {
            val v = tile.groundItemPile ?: return null
            return SceneElement.ItemPile(v, tile.plane)
        }
    }

    object Game : TileObjects.Many<SceneElement.Game>(Client.accessor.scene) {

        override val additions: Observable<SceneElement.Game> = XScene.newGameObject.exit
                .filter { it.returned }
                .map {
                    val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
                    SceneElement.Game(tile.gameObjects[tile.gameObjectsCount - 1])
                }

        override val removals: Observable<SceneElement.Game> = XScene.removeGameObject.enter
                .map { SceneElement.Game(it.arguments[0] as XGameObject) }
                .delay { XClient.doCycle.enter }

        override fun fromTile(tile: XTile): List<SceneElement.Game> {
            val gos = tile.gameObjects ?: return emptyList()
            val list = ArrayList<SceneElement.Game>(tile.gameObjectsCount)
            for (i in 0 until tile.gameObjectsCount) {
                gos[i]?.let { list.add(SceneElement.Game(it)) }
            }
            return list
        }
    }
}
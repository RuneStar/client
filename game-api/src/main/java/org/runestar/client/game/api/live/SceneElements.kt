package org.runestar.client.game.api.live

import io.reactivex.Observable
import org.runestar.client.game.api.SceneElement
import org.runestar.client.game.api.TileObjects
import org.runestar.client.game.api.utils.addNotNull
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.access.XGameObject
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.game.raw.access.XTile
import org.runestar.client.game.raw.CLIENT

object SceneElements : TileObjects.Many<SceneElement>(CLIENT.scene) {

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

    override val additions: Observable<SceneElement> = Observable.mergeArray(
            Wall.additions,
            Floor.additions,
            Boundary.additions,
            ItemPile.additions,
            Game.additions
    )

    override val removals: Observable<SceneElement> = Observable.mergeArray(
            Wall.removals,
            Floor.removals,
            Boundary.removals,
            ItemPile.removals,
            Game.removals
    )

    object Object : TileObjects.Many<SceneElement>(CLIENT.scene) {

        override val additions: Observable<SceneElement> = Observable.mergeArray(
                Wall.additions,
                Floor.additions,
                Boundary.additions,
                Game.additions.filter(SceneElement::isObject)
        )

        override val removals: Observable<SceneElement> = Observable.mergeArray(
                Wall.removals,
                Floor.removals,
                Boundary.removals,
                Game.removals.filter(SceneElement::isObject)
        )

        override fun fromTile(tile: XTile): List<SceneElement> {
            val list = ArrayList<SceneElement>()
            list.addNotNull(Floor.fromTile(tile))
            list.addNotNull(Wall.fromTile(tile))
            list.addNotNull(Boundary.fromTile(tile))
            Game.fromTile(tile).filterTo(list, SceneElement::isObject)
            return list
        }
    }

    object Wall : TileObjects.Single<SceneElement.Wall>(CLIENT.scene) {

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

    object Floor : TileObjects.Single<SceneElement.Floor>(CLIENT.scene) {

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

    object Boundary : TileObjects.Single<SceneElement.Boundary>(CLIENT.scene) {

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

    object ItemPile : TileObjects.Single<SceneElement.ItemPile>(CLIENT.scene) {

        override val additions: Observable<SceneElement.ItemPile> = XScene.newObjStack.exit.map {
            val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
            SceneElement.ItemPile(tile.objStack, tile.plane)
        }

        override val removals: Observable<SceneElement.ItemPile> =
                Observable.merge(XScene.removeObjStack.enter, XScene.newObjStack.enter)
                .filter { getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int) != null }
                .map { checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int)) }
                .filter { it.objStack != null }
                .map { SceneElement.ItemPile(it.objStack, it.plane) }

        override fun fromTile(tile: XTile): SceneElement.ItemPile? {
            val v = tile.objStack ?: return null
            return SceneElement.ItemPile(v, tile.plane)
        }
    }

    object Game : TileObjects.Many<SceneElement.Game>(CLIENT.scene) {

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
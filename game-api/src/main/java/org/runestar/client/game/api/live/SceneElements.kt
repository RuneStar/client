package org.runestar.client.game.api.live

import io.reactivex.Observable
import org.runestar.client.game.api.SceneElement
import org.runestar.client.game.api.TileObjects
import org.runestar.client.game.api.utils.addNotNull
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.game.raw.access.XTile
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XScenery

object SceneElements : TileObjects.Many<SceneElement>(CLIENT.scene) {

    override fun fromTile(tile: XTile): List<SceneElement> {
        val list = ArrayList<SceneElement>()
        list.addNotNull(FloorDecoration.fromTile(tile))
        list.addNotNull(WallDecoration.fromTile(tile))
        list.addNotNull(Wall.fromTile(tile))
        list.addNotNull(ObjStack.fromTile(tile))
        list.addAll(Scenery.fromTile(tile))
        return list
    }

    val clears: Observable<Unit> = XScene.clear.exit.map { Unit }

    override val additions: Observable<SceneElement> = Observable.mergeArray(
            WallDecoration.additions,
            FloorDecoration.additions,
            Wall.additions,
            ObjStack.additions,
            Scenery.additions
    )

    override val removals: Observable<SceneElement> = Observable.mergeArray(
            WallDecoration.removals,
            FloorDecoration.removals,
            Wall.removals,
            ObjStack.removals,
            Scenery.removals
    )

    object Loc : TileObjects.Many<SceneElement>(CLIENT.scene) {

        override val additions: Observable<SceneElement> = Observable.mergeArray(
                WallDecoration.additions,
                FloorDecoration.additions,
                Wall.additions,
                Scenery.additions.filter(SceneElement::isLoc)
        )

        override val removals: Observable<SceneElement> = Observable.mergeArray(
                WallDecoration.removals,
                FloorDecoration.removals,
                Wall.removals,
                Scenery.removals.filter(SceneElement::isLoc)
        )

        override fun fromTile(tile: XTile): List<SceneElement> {
            val list = ArrayList<SceneElement>()
            list.addNotNull(FloorDecoration.fromTile(tile))
            list.addNotNull(WallDecoration.fromTile(tile))
            list.addNotNull(Wall.fromTile(tile))
            Scenery.fromTile(tile).filterTo(list, SceneElement::isLoc)
            return list
        }
    }

    object WallDecoration : TileObjects.Single<SceneElement.WallDecoration>(CLIENT.scene) {

        override val additions: Observable<SceneElement.WallDecoration> = XScene.newWallDecoration.exit.map {
                    val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
                    SceneElement.WallDecoration(tile.wallDecoration, tile.plane)
        }

        override val removals: Observable<SceneElement.WallDecoration> = XScene.removeWallDecoration.enter.map {
            val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
            SceneElement.WallDecoration(tile.wallDecoration, tile.plane)
        }

        override fun fromTile(tile: XTile): SceneElement.WallDecoration? {
            val v = tile.wallDecoration ?: return null
            return SceneElement.WallDecoration(v, tile.plane)
        }
    }

    object FloorDecoration : TileObjects.Single<SceneElement.FloorDecoration>(CLIENT.scene) {

        override val additions: Observable<SceneElement.FloorDecoration> = XScene.newFloorDecoration.exit.map {
                    val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
                    SceneElement.FloorDecoration(tile.floorDecoration, tile.plane)
        }

        override val removals: Observable<SceneElement.FloorDecoration> = XScene.removeFloorDecoration.enter.map {
            val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
            SceneElement.FloorDecoration(tile.floorDecoration, tile.plane)
        }

        override fun fromTile(tile: XTile): SceneElement.FloorDecoration? {
            val v = tile.floorDecoration ?: return null
            return SceneElement.FloorDecoration(v, tile.plane)
        }
    }

    object Wall : TileObjects.Single<SceneElement.Wall>(CLIENT.scene) {

        override val additions: Observable<SceneElement.Wall> = XScene.newWall.exit.map {
                    val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
                    SceneElement.Wall(tile.wall, tile.plane)
        }

        override val removals: Observable<SceneElement.Wall> = XScene.removeWall.enter.map {
            val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
            SceneElement.Wall(tile.wall, tile.plane)
        }

        override fun fromTile(tile: XTile): SceneElement.Wall? {
            val v = tile.wall ?: return null
            return SceneElement.Wall(v, tile.plane)
        }
    }

    object ObjStack : TileObjects.Single<SceneElement.ObjStack>(CLIENT.scene) {

        override val additions: Observable<SceneElement.ObjStack> = XScene.newObjStack.exit.map {
            val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
            SceneElement.ObjStack(tile.objStack, tile.plane)
        }

        override val removals: Observable<SceneElement.ObjStack> =
                Observable.merge(XScene.removeObjStack.enter, XScene.newObjStack.enter)
                .filter { getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int) != null }
                .map { checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int)) }
                .filter { it.objStack != null }
                .map { SceneElement.ObjStack(it.objStack, it.plane) }

        override fun fromTile(tile: XTile): SceneElement.ObjStack? {
            val v = tile.objStack ?: return null
            return SceneElement.ObjStack(v, tile.plane)
        }
    }

    object Scenery : TileObjects.Many<SceneElement.Scenery>(CLIENT.scene) {

        override val additions: Observable<SceneElement.Scenery> = XScene.newScenery.exit
                .filter { it.returned }
                .map {
                    val tile = checkNotNull(getTile(it.arguments[0] as Int, it.arguments[1] as Int, it.arguments[2] as Int))
                    SceneElement.Scenery(tile.scenery[tile.sceneryCount - 1])
                }

        override val removals: Observable<SceneElement.Scenery> = XScene.removeScenery.enter
                .map { SceneElement.Scenery(it.arguments[0] as XScenery) }
                .delay { XClient.doCycle.enter }

        override fun fromTile(tile: XTile): List<SceneElement.Scenery> {
            val gos = tile.scenery ?: return emptyList()
            val list = ArrayList<SceneElement.Scenery>(tile.sceneryCount)
            for (i in 0 until tile.sceneryCount) {
                gos[i]?.let { list.add(SceneElement.Scenery(it)) }
            }
            return list
        }
    }
}
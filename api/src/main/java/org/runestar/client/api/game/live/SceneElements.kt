package org.runestar.client.api.game.live

import io.reactivex.rxjava3.core.Observable
import org.runestar.client.api.game.SceneElement
import org.runestar.client.raw.access.XClient
import org.runestar.client.raw.access.XScene
import org.runestar.client.raw.access.XTile
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XScenery
import java.util.Optional

interface SceneElementEvents<T : SceneElement> {

    val added: Observable<T>

    val removed: Observable<T>
}

object SceneElements : SceneElementEvents<SceneElement> {

    val cleared: Observable<Unit> = XScene.clear.exit.map { Unit }

    override val added: Observable<SceneElement> = Observable.mergeArray(
            WallDecoration.added,
            FloorDecoration.added,
            Wall.added,
            ObjStack.added,
            Scenery.added
    )

    override val removed: Observable<SceneElement> = Observable.mergeArray(
            WallDecoration.removed,
            FloorDecoration.removed,
            Wall.removed,
            ObjStack.removed,
            Scenery.removed
    )

    object Loc : SceneElementEvents<SceneElement> {

        override val added: Observable<SceneElement> = Observable.mergeArray(
                WallDecoration.added,
                FloorDecoration.added,
                Wall.added,
                Scenery.added.filter(SceneElement::isLoc)
        )

        override val removed: Observable<SceneElement> = Observable.mergeArray(
                WallDecoration.removed,
                FloorDecoration.removed,
                Wall.removed,
                Scenery.removed.filter(SceneElement::isLoc)
        )
    }

    object WallDecoration : SceneElementEvents<SceneElement.WallDecoration> {

        override val added: Observable<SceneElement.WallDecoration> = XScene.newWallDecoration.exit.map {
            val tile = checkNotNull(getTile(it.arguments))
            SceneElement.WallDecoration(tile.wallDecoration, tile.plane)
        }

        override val removed: Observable<SceneElement.WallDecoration> = XScene.removeWallDecoration.enter.map {
            val tile = checkNotNull(getTile(it.arguments))
            SceneElement.WallDecoration(tile.wallDecoration, tile.plane)
        }
    }

    object FloorDecoration : SceneElementEvents<SceneElement.FloorDecoration> {

        override val added: Observable<SceneElement.FloorDecoration> = XScene.newFloorDecoration.exit.map {
            val tile = checkNotNull(getTile(it.arguments))
            SceneElement.FloorDecoration(tile.floorDecoration, tile.plane)
        }

        override val removed: Observable<SceneElement.FloorDecoration> = XScene.removeFloorDecoration.enter.map {
            val tile = checkNotNull(getTile(it.arguments))
            SceneElement.FloorDecoration(tile.floorDecoration, tile.plane)
        }
    }

    object Wall : SceneElementEvents<SceneElement.Wall> {

        override val added: Observable<SceneElement.Wall> = XScene.newWall.exit.map {
            val tile = checkNotNull(getTile(it.arguments))
            SceneElement.Wall(tile.wall, tile.plane)
        }

        override val removed: Observable<SceneElement.Wall> = XScene.removeWall.enter.map {
            val tile = checkNotNull(getTile(it.arguments))
            SceneElement.Wall(tile.wall, tile.plane)
        }
    }

    object ObjStack : SceneElementEvents<SceneElement.ObjStack> {

        override val added: Observable<SceneElement.ObjStack> = XScene.newObjStack.exit.map {
            val tile = checkNotNull(getTile(it.arguments))
            SceneElement.ObjStack(tile.objStack, tile.plane)
        }

        override val removed: Observable<SceneElement.ObjStack> = Observable.merge(XScene.removeObjStack.enter, XScene.newObjStack.enter)
                .map { Optional.ofNullable(getTile(it.arguments)) }
                .filter { it.isPresent }
                .map { it.get() }
                .filter { it.objStack != null }
                .map { SceneElement.ObjStack(it.objStack, it.plane) }
    }

    object Scenery : SceneElementEvents<SceneElement.Scenery> {

        override val added: Observable<SceneElement.Scenery> = XScene.newScenery.exit
                .filter { it.returned }
                .map {
                    val tile = checkNotNull(getTile(it.arguments))
                    SceneElement.Scenery(tile.scenery[tile.sceneryCount - 1])
                }

        override val removed: Observable<SceneElement.Scenery> = XScene.removeScenery.enter
                .map { SceneElement.Scenery(it.arguments[0] as XScenery) }
                .delay { XClient.doCycle.enter }
    }

    private fun getTile(args: Array<*>): XTile? = CLIENT.scene.tiles[args[0] as Int][args[1] as Int][args[2] as Int]
}
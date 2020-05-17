package org.runestar.client.api.game

import org.runestar.client.api.game.live.Scene
import org.runestar.client.api.util.cascadingListOf
import org.runestar.client.raw.access.XWall
import org.runestar.client.raw.access.XEntity
import org.runestar.client.raw.access.XFloorDecoration
import org.runestar.client.raw.access.XModel
import org.runestar.client.raw.access.XNode
import org.runestar.client.raw.access.XObj
import org.runestar.client.raw.access.XObjStack
import org.runestar.client.raw.access.XScenery
import org.runestar.client.raw.access.XWallDecoration
import org.runestar.client.raw.base.Accessor

abstract class SceneElement(accessor: Accessor) : Wrapper(accessor) {

    private companion object {

        fun xModelFromEntity(e: XEntity?): XModel? {
            return e as? XModel ?: e?.model
        }

        inline fun makeModel(e: XEntity?, f: (XModel) -> Model): Model? {
            return xModelFromEntity(e)?.let(f)
        }
    }

    abstract val plane: Int

    abstract val tag: SceneElementTag

    val x: Int get() = tag.x

    val y: Int get() = tag.y

    val location: SceneTile get() = SceneTile(x, y, plane)

    val isLoc: Boolean get() = tag.kind == SceneElementKind.LOC

    // todo
//    val baseOrientation: Angle get() = Angle.of(((flags shr 6) and 3) * 512)

    abstract val dynamicOrientation: Angle

//    val orientation: Angle get() = baseOrientation + dynamicOrientation

    abstract val modelPosition: Position

    protected abstract val entity: XEntity?

    val model: Model? get() = makeModel(entity) { Model(it, modelPosition, dynamicOrientation) }

    open val models: List<Model> get() = cascadingListOf(model)

    abstract class TwoModels(
            accessor: Accessor
    ) : SceneElement(accessor) {

        protected abstract val entity2: XEntity?

        abstract val modelPosition2: Position

        val model2: Model? get() = makeModel(entity2) { Model(it, modelPosition2, dynamicOrientation) }

        override val models: List<Model> get() = cascadingListOf(model, model2)
    }

    abstract class ThreeModels(
            accessor: Accessor
    ) : TwoModels(accessor) {

        protected abstract val entity3: XEntity?

        abstract val modelPosition3: Position

        val model3: Model? get() = makeModel(entity3) { Model(it, modelPosition3, dynamicOrientation) }

        override val models: List<Model> get() = cascadingListOf(model, model2, model3)
    }

    class Scenery(
            override val accessor: XScenery
    ) : SceneElement(accessor) {

        override val dynamicOrientation: Angle get() = Angle.of(accessor.orientation)

        override val modelPosition: Position get() = Position(accessor.centerX, accessor.centerY, heightOffset, plane)

        val heightOffset: Int get() = Scene.getTileHeight(accessor.centerX, accessor.centerY, plane) - accessor.height

        override val entity: XEntity? get() = accessor.entity

        override val plane: Int get() = accessor.plane

        override val tag get() = SceneElementTag(accessor.tag)

        override fun toString(): String = "SceneElement.Scenery($tag)"
    }

    class FloorDecoration(
            override val accessor: XFloorDecoration,
            override val plane: Int
    ) : SceneElement(accessor) {

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val modelPosition: Position get() = Position.centerOfTile(x, y, 0, plane)

        override val entity: XEntity? get() = accessor.entity

        override val tag get() = SceneElementTag(accessor.tag)

        override fun toString(): String = "SceneElement.FloorDecoration($tag)"
    }

    class WallDecoration(
            override val accessor: XWallDecoration,
            override val plane: Int
    ) : TwoModels(accessor) {

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val modelPosition: Position get() = Position(
                LocalValue(x, LocalValue.MID_SUB).value + accessor.xOffset,
                LocalValue(y, LocalValue.MID_SUB).value + accessor.yOffset,
                0,
                plane
        )

        override val entity: XEntity? get() = accessor.entity1

        override val modelPosition2: Position get() = Position.centerOfTile(x, y, 0, plane)

        override val entity2: XEntity? get() = accessor.entity2

        override val tag get() = SceneElementTag(accessor.tag)

        override fun toString(): String = "SceneElement.WallDecoration($tag)"
    }

    class Wall(
            override val accessor: XWall,
            override val plane: Int
    ) : TwoModels(accessor) {

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val modelPosition: Position get() = Position.centerOfTile(x, y, 0, plane)

        override val modelPosition2: Position get() = modelPosition

        override val entity: XEntity? get() = accessor.entity1

        override val entity2: XEntity? get() = accessor.entity2

        override val tag get() = SceneElementTag(accessor.tag)

        override fun toString(): String = "SceneElement.Wall($tag)"
    }

    class ObjStack(
            override val accessor: XObjStack,
            override val plane: Int
    ) : ThreeModels(accessor), Iterable<GroundItem> {

        override val tag get() = SceneElementTag(accessor.tag)

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val entity: XEntity? get() = accessor.first

        override val modelPosition: Position get() = Position.centerOfTile(x, y, accessor.height, plane)

        override val entity2: XEntity? get() = accessor.second

        override val modelPosition2: Position get() = modelPosition

        override val entity3: XEntity? get() = accessor.third

        override val modelPosition3: Position get() = modelPosition

        val first: GroundItem? get() = accessor.first?.let { GroundItem(it as XObj, modelPosition) }

        val second: GroundItem? get() = accessor.second?.let { GroundItem(it as XObj, modelPosition) }

        val third: GroundItem? get() = accessor.third?.let { GroundItem(it as XObj, modelPosition) }

        override fun iterator(): Iterator<GroundItem> = object : AbstractIterator<GroundItem>() {

            private val position = modelPosition

            private var cur: XNode? = accessor.first

            override fun computeNext() {
                val gi = cur as? XObj ?: return done()
                setNext(GroundItem(gi, position))
                cur = gi.previous
            }
        }

        override fun toString(): String = "SceneElement.ObjStack($tag)"
    }
}
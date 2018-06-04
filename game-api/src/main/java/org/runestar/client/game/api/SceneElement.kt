package org.runestar.client.game.api

import org.runestar.client.game.api.utils.cascadingListOf
import org.runestar.client.game.raw.Accessor
import org.runestar.client.game.raw.access.*

abstract class SceneElement(accessor: Accessor) : Wrapper(accessor) {

    private companion object {

        fun xModelFromEntity(e: XEntity?): XModel? {
            return e as? XModel ?: e?.model
        }

        inline fun makeModel(e: XEntity?, f: (XModel) -> Model): Model? {
            return xModelFromEntity(e)?.let(f)
        }
    }

    protected abstract val tagPacked: Long

    abstract val plane: Int

    val tag: SceneElementTag get() = SceneElementTag(tagPacked, plane)

    val id: Int get() = SceneElementTag.getId(tagPacked)

    val x: Int get() = SceneElementTag.getX(tagPacked)

    val y: Int get() = SceneElementTag.getY(tagPacked)

    val location: SceneTile get() = SceneElementTag.getLocation(tagPacked, plane)

    val isInteractable: Boolean get() = SceneElementTag.isInteractable(tagPacked)

    val kind: SceneElementKind get() = SceneElementTag.getKind(tagPacked)

    val isObject: Boolean get() = kind == SceneElementKind.OBJECT

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

    class Game(
            override val accessor: XGameObject
    ) : SceneElement(accessor) {

        override val dynamicOrientation: Angle get() = Angle.of(accessor.orientation)

        // todo: height
        override val modelPosition: Position get() = Position(accessor.centerX, accessor.centerY, 0, plane)

        override val entity: XEntity? get() = accessor.entity

        override val plane: Int get() = accessor.plane

        override val tagPacked get() = accessor.tag

        override fun toString(): String = "SceneElement.Game(tag=$tag)"
    }

    class Floor(
            override val accessor: XFloorDecoration,
            override val plane: Int
    ) : SceneElement(accessor) {

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val modelPosition: Position get() = Position.centerOfTile(x, y, 0, plane)

        override val entity: XEntity? get() = accessor.entity

        override val tagPacked get() = accessor.tag

        override fun toString(): String = "SceneElement.Floor(tag=$tag)"
    }

    class Wall(
            override val accessor: XWallDecoration,
            override val plane: Int
    ) : TwoModels(accessor) {

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val modelPosition: Position get() = Position(
                Position.toLocal(x, Position.MID_SUB) + accessor.xOffset,
                Position.toLocal(y, Position.MID_SUB) + accessor.yOffset,
                0,
                plane
        )

        override val entity: XEntity? get() = accessor.entity1

        override val modelPosition2: Position get() = Position.centerOfTile(x, y, 0, plane)

        override val entity2: XEntity? get() = accessor.entity2

        override val tagPacked get() = accessor.tag

        override fun toString(): String = "SceneElement.Wall(tag=$tag)"
    }

    class Boundary(
            override val accessor: XBoundaryObject,
            override val plane: Int
    ) : TwoModels(accessor) {

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val modelPosition: Position get() = Position.centerOfTile(x, y, 0, plane)

        override val modelPosition2: Position get() = modelPosition

        override val entity: XEntity? get() = accessor.entity1

        override val entity2: XEntity? get() = accessor.entity2

        override val tagPacked get() = accessor.tag

        override fun toString(): String = "SceneElement.Boundary(tag=$tag)"
    }

    class ItemPile(
            override val accessor: XGroundItemPile,
            override val plane: Int
    ) : ThreeModels(accessor), Iterable<GroundItem> {

        override val tagPacked get() = accessor.tag

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val entity: XEntity? get() = accessor.first

        override val modelPosition: Position get() = Position.centerOfTile(x, y, accessor.height, plane)

        override val entity2: XEntity? get() = accessor.second

        override val modelPosition2: Position get() = modelPosition

        override val entity3: XEntity? get() = accessor.third

        override val modelPosition3: Position get() = modelPosition

        val first: GroundItem? get() = accessor.first?.let { GroundItem(it as XGroundItem, modelPosition) }

        val second: GroundItem? get() = accessor.second?.let { GroundItem(it as XGroundItem, modelPosition) }

        val third: GroundItem? get() = accessor.third?.let { GroundItem(it as XGroundItem, modelPosition) }

        override fun iterator(): Iterator<GroundItem> = object : AbstractIterator<GroundItem>() {

            private val position = modelPosition

            private var cur: XNode? = accessor.first

            override fun computeNext() {
                val gi = cur as? XGroundItem ?: return done()
                setNext(GroundItem(gi, position))
                cur = gi.previous
            }
        }

        override fun toString(): String = "SceneElement.ItemPile(tag=$tag)"
    }
}
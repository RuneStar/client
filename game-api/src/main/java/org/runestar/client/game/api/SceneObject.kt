package org.runestar.client.game.api

import org.runestar.client.game.raw.Accessor
import org.runestar.client.game.raw.access.*

abstract class SceneObject(accessor: Accessor) : Wrapper(accessor) {

    private companion object {

        fun xModelFromEntity(e: XEntity?): XModel? {
            return e as? XModel ?: e?.model
        }

        inline fun makeModel(e: XEntity?, f: (XModel) -> Model): Model? {
            return xModelFromEntity(e)?.let(f)
        }

        fun <T> cascadingList(a: T?): List<T> {
            val m1 = a ?: return listOf()
            return listOf(m1)
        }

        fun <T> cascadingList(a: T?, b: T?): List<T> {
            val m1 = a ?: return listOf()
            val m2 = b ?: return listOf(m1)
            return listOf(m1, m2)
        }

        fun <T> cascadingList(a: T?, b: T?, c: T?): List<T> {
            val m1 = a ?: return listOf()
            val m2 = b ?: return listOf(m1)
            val m3 = c ?: return listOf(m1, m2)
            return listOf(m1, m2, m3)
        }
    }

    protected abstract val tagPacked: Long

    abstract val plane: Int

    val tag: EntityTag get() = EntityTag(tagPacked, plane)

    val id: Int get() = EntityTag.getId(tagPacked)

    val x: Int get() = EntityTag.getX(tagPacked)

    val y: Int get() = EntityTag.getY(tagPacked)

    val location: SceneTile get() = EntityTag.getLocation(tagPacked, plane)

    val isInteractable: Boolean get() = EntityTag.isInteractable(tagPacked)

    val kind: EntityKind get() = EntityTag.getEntityKind(tagPacked)

    val isObject: Boolean get() = kind == EntityKind.OBJECT

    // todo
//    val baseOrientation: Angle get() = Angle.of(((flags shr 6) and 3) * 512)

    abstract val dynamicOrientation: Angle

//    val orientation: Angle get() = baseOrientation + dynamicOrientation

    abstract val modelPosition: Position

    protected abstract val entity: XEntity?

    val model: Model? get() = makeModel(entity) { Model(it, modelPosition, dynamicOrientation) }

    open val models: List<Model> get() = cascadingList(model)

    abstract class TwoModels(
            accessor: Accessor
    ) : SceneObject(accessor) {

        protected abstract val entity2: XEntity?

        abstract val modelPosition2: Position

        val model2: Model? get() = makeModel(entity2) { Model(it, modelPosition2, dynamicOrientation) }

        override val models: List<Model> get() = cascadingList(model, model2)
    }

    abstract class ThreeModels(
            accessor: Accessor
    ) : TwoModels(accessor) {

        protected abstract val entity3: XEntity?

        abstract val modelPosition3: Position

        val model3: Model? get() = makeModel(entity3) { Model(it, modelPosition3, dynamicOrientation) }

        override val models: List<Model> get() = cascadingList(model, model2, model3)
    }

    class Game(
            override val accessor: XGameObject
    ) : SceneObject(accessor) {

        override val dynamicOrientation: Angle get() = Angle.of(accessor.orientation)

        override val modelPosition: Position get() = Position(accessor.centerX, accessor.centerY, 0, plane)

        override val entity: XEntity? get() = accessor.entity

        override val plane: Int get() = accessor.plane

        override val tagPacked get() = accessor.tag

        override fun toString(): String = "SceneObject.Game(tag=$tag)"
    }

    class Floor(
            override val accessor: XFloorDecoration,
            override val plane: Int
    ) : SceneObject(accessor) {

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val modelPosition: Position get() = Position.centerOfTile(x, y, 0, plane)

        override val entity: XEntity? get() = accessor.entity

        override val tagPacked get() = accessor.tag

        override fun toString(): String = "SceneObject.Floor(tag=$tag)"
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

        override fun toString(): String = "SceneObject.Wall(tag=$tag)"
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

        override fun toString(): String = "SceneObject.Boundary(tag=$tag)"
    }

    class ItemPile(
            override val accessor: XGroundItemPile,
            override val plane: Int
    ) : ThreeModels(accessor) {

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

        override fun toString(): String = "SceneObject.ItemPile(tag=$tag)"
    }
}
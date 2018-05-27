package org.runestar.client.game.api

import org.runestar.client.game.raw.Accessor
import org.runestar.client.game.raw.access.*
import java.util.*

abstract class SceneObject(accessor: Accessor) : Wrapper(accessor) {

    private companion object {

        fun xModelFromEntity(e: XEntity?): XModel? {
            return e as? XModel ?: e?.model
        }
    }

    abstract val tag: EntityTag

    abstract val flags: Int

    val id: Int get() = tag.id

    val location: SceneTile get() = tag.location

    val isInteractable: Boolean get() = tag.isInteractable

    // todo
//    val baseOrientation: Angle get() = Angle.of(((flags shr 6) and 3) * 512)

    abstract val dynamicOrientation: Angle

//    val orientation: Angle get() = baseOrientation + dynamicOrientation

    abstract val position: Position

    protected abstract val entity: XEntity?

    val model: Model? get() = xModelFromEntity(entity)?.let { Model(it, position, dynamicOrientation) }

    abstract val models: List<Model>

    abstract class OneModel(
            accessor: Accessor
    ) : SceneObject(accessor) {

        final override val models: List<Model> get() = model?.let { Collections.singletonList(it) } ?: emptyList()
    }

    abstract class TwoModels(
            accessor: Accessor
    ) : SceneObject(accessor) {

        protected abstract val entity2: XEntity?

        abstract val position2: Position

        val model2: Model? get() = xModelFromEntity(entity2)?.let { Model(it, position2, dynamicOrientation) }

        final override val models: List<Model> get() {
            val m1 = model ?: return emptyList()
            val m2 = model2 ?: return Collections.singletonList(m1)
            return listOf(m1, m2)
        }
    }

    class Game(
            override val accessor: XGameObject
    ) : OneModel(accessor) {

        override val flags: Int get() = accessor.flags

        override val dynamicOrientation: Angle get() = Angle.of(accessor.orientation)

        override val position: Position get() = Position(accessor.centerX, accessor.centerY, 0, location.plane)

        override val entity: XEntity? get() = accessor.entity

        override val tag get() = EntityTag(accessor.tag, accessor.plane)

        override fun toString(): String {
            return "SceneObject.Game(tag=$tag)"
        }
    }

    class Floor(
            override val accessor: XFloorDecoration,
            val plane: Int
    ) : OneModel(accessor) {

        override val flags: Int get() = accessor.flags

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val position: Position get() = location.center

        override val entity: XEntity? get() = accessor.entity

        override val tag get() = EntityTag(accessor.tag, plane)

        override fun toString(): String {
            return "SceneObject.Floor(tag=$tag)"
        }
    }

    class Wall(
            override val accessor: XWallDecoration,
            val plane: Int
    ) : TwoModels(accessor) {

        override val flags: Int get() = accessor.flags

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val position: Position get() = location.center.plusLocal(accessor.xOffset, accessor.yOffset, 0)

        override val entity: XEntity? get() = accessor.entity1

        override val position2: Position get() = location.center

        override val entity2: XEntity? get() = accessor.entity2

        override val tag get() = EntityTag(accessor.tag, plane)

        override fun toString(): String {
            return "SceneObject.Wall(tag=$tag)"
        }
    }

    class Boundary(
            override val accessor: XBoundaryObject,
            val plane: Int
    ) : TwoModels(accessor) {

        override val flags: Int get() = accessor.flags

        override val dynamicOrientation: Angle get() = Angle.ZERO

        override val position: Position get() = location.center

        override val position2: Position get() = position

        override val entity: XEntity? get() = accessor.entity1

        override val entity2: XEntity? get() = accessor.entity2

        override val tag get() = EntityTag(accessor.tag, plane)

        override fun toString(): String {
            return "SceneObject.Boundary(tag=$tag)"
        }
    }
}
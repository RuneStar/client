package org.runestar.client.game.api

import org.runestar.client.game.raw.Accessor
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.*
import java.util.*

abstract class SceneObject(accessor: Accessor) : Wrapper(accessor) {

    private companion object {

        private fun xModelFromEntity(e: XEntity?): XModel? {
            return e as? XModel ?: e?.model
        }

        private fun <T> asListOrEmpty(t: T?): List<T> {
            return t?.let { Collections.singletonList(t) } ?: emptyList()
        }
    }

    abstract val tag: EntityTag

    val id: Int get() = tag.id

    val location: SceneTile get() = tag.location

    val isInteractable: Boolean get() = tag.isInteractable

    abstract val orientation: Angle

    abstract val position: Position

    abstract val primaryModel: Model?

    abstract val secondaryModel: Model?

    abstract val models: List<Model>

    class Game(
            override val accessor: XGameObject
    ) : SceneObject(accessor) {

        override val orientation: Angle get() = Angle(accessor.orientation)

        override val position: Position get() = Position(accessor.centerX, accessor.centerY, 0, location.plane)

        override val primaryModel: Model? get() {
            val m = xModelFromEntity(accessor.entity) ?: return null
            return Model(m, position, orientation)
        }

        override val secondaryModel: Model? = null

        override val models: List<Model> get() = asListOrEmpty(primaryModel)

        override val tag get() = EntityTag(accessor.tag, accessor.plane)

        override fun toString(): String {
            return "SceneObject.Game(tag=$tag)"
        }
    }

    class Floor(
            override val accessor: XFloorDecoration,
            val plane: Int = Client.accessor.plane
    ) : SceneObject(accessor) {

        override val orientation: Angle get() = Angle.ZERO

        override val position: Position get() = location.center

        override val primaryModel: Model? get() {
            val m = xModelFromEntity(accessor.entity) ?: return null
            return Model(m, position, orientation)
        }

        override val secondaryModel: Model? = null

        override val models: List<Model> get() = asListOrEmpty(primaryModel)

        override val tag get() = EntityTag(accessor.tag, plane)

        override fun toString(): String {
            return "SceneObject.Floor(tag=$tag)"
        }
    }

    class Wall(
            override val accessor: XWallDecoration,
            val plane: Int = Client.accessor.plane
    ) : SceneObject(accessor) {

        override val orientation: Angle get() = Angle.ZERO

        // todo: this is model1 position, model2 is location.center
        override val position: Position get() = location.center.plusLocal(accessor.xOffset, accessor.yOffset, 0)

        override val primaryModel: Model? get() {
            val m = xModelFromEntity(accessor.entity1) ?: return null
            return Model(m, position, orientation)
        }

        override val secondaryModel: Model? get() {
            val m = xModelFromEntity(accessor.entity2) ?: return null
            return Model(m, location.center, orientation)
        }

        override val models: List<Model> get() {
            val m1 = primaryModel ?: return emptyList()
            val m2 = secondaryModel ?: return Collections.singletonList(m1)
            return listOf(m1, m2)
        }

        override val tag get() = EntityTag(accessor.tag, plane)

        override fun toString(): String {
            return "SceneObject.Wall(tag=$tag)"
        }
    }

    class Boundary(
            override val accessor: XBoundaryObject,
            val plane: Int = Client.accessor.plane
    ) : SceneObject(accessor) {

        override val orientation: Angle get() = Angle.ZERO

        override val position: Position get() = location.center

        override val primaryModel: Model? get() {
            val m = accessor.entity1 as? XModel ?: accessor.entity1?.model ?: return null
            return Model(m, position, orientation)
        }

        override val secondaryModel: Model? get() {
            val m = accessor.entity2 as? XModel ?: accessor.entity2?.model ?: return null
            return Model(m, position, orientation)
        }

        override val models: List<Model> get() {
            val m1 = primaryModel ?: return emptyList()
            val m2 = secondaryModel ?: return Collections.singletonList(m1)
            return listOf(m1, m2)
        }

        override val tag get() = EntityTag(accessor.tag, plane)

        override fun toString(): String {
            return "SceneObject.Boundary(tag=$tag)"
        }
    }
}
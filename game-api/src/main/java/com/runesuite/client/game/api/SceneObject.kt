package com.runesuite.client.game.api

import com.runesuite.client.game.api.live.Game
import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XBoundaryObject
import com.runesuite.client.game.raw.access.XFloorDecoration
import com.runesuite.client.game.raw.access.XGameObject
import com.runesuite.client.game.raw.access.XWallDecoration

abstract class SceneObject : Wrapper() {

    abstract val tag: EntityTag

    val location get() = tag.location

    class Interactable(
            override val accessor: XGameObject
    ) : SceneObject() {

        override val tag get() = EntityTag(accessor.tag, accessor.plane)

        override fun toString(): String {
            return "SceneObject.Interactable(tag=$tag)"
        }
    }

    class Floor(
            override val accessor: XFloorDecoration,
            val plane: Int = Client.accessor.plane
    ) : SceneObject() {

        override val tag get() = EntityTag(accessor.tag, plane)

        override fun toString(): String {
            return "SceneObject.Floor(tag=$tag)"
        }
    }

    class Wall(
            override val accessor: XWallDecoration,
            val plane: Int = Client.accessor.plane
    ) : SceneObject() {

        override val tag get() = EntityTag(accessor.tag, plane)

        override fun toString(): String {
            return "SceneObject.Wall(tag=$tag)"
        }
    }

    class Boundary(
            override val accessor: XBoundaryObject,
            val plane: Int = Client.accessor.plane
    ) : SceneObject() {

        override val tag get() = EntityTag(accessor.tag, plane)

        override fun toString(): String {
            return "SceneObject.Boundary(tag=$tag)"
        }
    }
}
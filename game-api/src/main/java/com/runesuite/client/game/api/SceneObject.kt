package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XBoundaryObject
import com.runesuite.client.game.raw.access.XFloorDecoration
import com.runesuite.client.game.raw.access.XGameObject
import com.runesuite.client.game.raw.access.XWallDecoration

abstract class SceneObject : Wrapper() {

    abstract val tag: EntityTag

    val location get() = tag.location

    class Interactable(override val accessor: XGameObject) : SceneObject() {
        override val tag get() = EntityTag(accessor.tag, accessor.plane)
    }

    class Floor(override val accessor: XFloorDecoration) : SceneObject() {
        override val tag get() = EntityTag(accessor.tag)
    }

    class Wall(override val accessor: XWallDecoration) : SceneObject() {
        override val tag get() = EntityTag(accessor.tag)
    }

    class Boundary(override val accessor: XBoundaryObject) : SceneObject() {
        override val tag get() = EntityTag(accessor.tag)
    }
}
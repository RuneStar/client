package com.runesuite.client.game

import com.runesuite.client.base.Wrapper
import com.runesuite.client.base.access.XBoundaryObject
import com.runesuite.client.base.access.XFloorDecoration
import com.runesuite.client.base.access.XGameObject
import com.runesuite.client.base.access.XWallDecoration

abstract class SceneObject(val location: SceneTile) : Wrapper() {

    abstract val tag: EntityTag

    class Interactable(override val accessor: XGameObject, location: SceneTile) : SceneObject(location) {
        override val tag get() = EntityTag(accessor.id)
    }

    class Floor(override val accessor: XFloorDecoration, location: SceneTile) : SceneObject(location) {
        override val tag get() = EntityTag(accessor.id)
    }

    class Wall(override val accessor: XWallDecoration, location: SceneTile) : SceneObject(location) {
        override val tag get() = EntityTag(accessor.id)
    }

    class Boundary(override val accessor: XBoundaryObject, location: SceneTile) : SceneObject(location) {
        override val tag get() = EntityTag(accessor.id)
    }
}
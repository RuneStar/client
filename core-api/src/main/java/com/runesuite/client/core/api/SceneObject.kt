package com.runesuite.client.core.api

import com.runesuite.client.core.raw.Wrapper
import com.runesuite.client.core.raw.access.XBoundaryObject
import com.runesuite.client.core.raw.access.XFloorDecoration
import com.runesuite.client.core.raw.access.XGameObject
import com.runesuite.client.core.raw.access.XWallDecoration

abstract class SceneObject(val location: com.runesuite.client.core.api.SceneTile) : Wrapper() {

    abstract val tag: com.runesuite.client.core.api.EntityTag

    class Interactable(override val accessor: XGameObject, location: com.runesuite.client.core.api.SceneTile) : com.runesuite.client.core.api.SceneObject(location) {
        override val tag get() = com.runesuite.client.core.api.EntityTag(accessor.id)
    }

    class Floor(override val accessor: XFloorDecoration, location: com.runesuite.client.core.api.SceneTile) : com.runesuite.client.core.api.SceneObject(location) {
        override val tag get() = com.runesuite.client.core.api.EntityTag(accessor.id)
    }

    class Wall(override val accessor: XWallDecoration, location: com.runesuite.client.core.api.SceneTile) : com.runesuite.client.core.api.SceneObject(location) {
        override val tag get() = com.runesuite.client.core.api.EntityTag(accessor.id)
    }

    class Boundary(override val accessor: XBoundaryObject, location: com.runesuite.client.core.api.SceneTile) : com.runesuite.client.core.api.SceneObject(location) {
        override val tag get() = com.runesuite.client.core.api.EntityTag(accessor.id)
    }
}
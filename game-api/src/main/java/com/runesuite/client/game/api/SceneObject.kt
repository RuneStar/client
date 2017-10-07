package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XBoundaryObject
import com.runesuite.client.game.raw.access.XFloorDecoration
import com.runesuite.client.game.raw.access.XGameObject
import com.runesuite.client.game.raw.access.XWallDecoration

abstract class SceneObject(val location: com.runesuite.client.game.api.SceneTile) : Wrapper() {

    abstract val tag: com.runesuite.client.game.api.EntityTag

    class Interactable(override val accessor: XGameObject, location: com.runesuite.client.game.api.SceneTile) : com.runesuite.client.game.api.SceneObject(location) {
        override val tag get() = com.runesuite.client.game.api.EntityTag(accessor.id)
    }

    class Floor(override val accessor: XFloorDecoration, location: com.runesuite.client.game.api.SceneTile) : com.runesuite.client.game.api.SceneObject(location) {
        override val tag get() = com.runesuite.client.game.api.EntityTag(accessor.id)
    }

    class Wall(override val accessor: XWallDecoration, location: com.runesuite.client.game.api.SceneTile) : com.runesuite.client.game.api.SceneObject(location) {
        override val tag get() = com.runesuite.client.game.api.EntityTag(accessor.id)
    }

    class Boundary(override val accessor: XBoundaryObject, location: com.runesuite.client.game.api.SceneTile) : com.runesuite.client.game.api.SceneObject(location) {
        override val tag get() = com.runesuite.client.game.api.EntityTag(accessor.id)
    }
}
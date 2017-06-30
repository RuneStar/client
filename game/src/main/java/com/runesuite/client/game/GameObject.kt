package com.runesuite.client.game

import com.runesuite.client.base.access.XGameObject
import com.runesuite.client.base.access.XModel

class GameObject(val accessor: XGameObject, val sceneTile: SceneTile) {

    val orientation = Angle(0)

    val pos = Position(accessor.centerX, accessor.centerY, 0, accessor.plane)

    val model: Model? get() {
        val entity = accessor.entity
        return when(entity) {
            null -> null
            is XModel -> Model(pos, orientation, entity)
            else -> null // Model(sceneTile.base, orientation, entity.model)
        }
    }
}
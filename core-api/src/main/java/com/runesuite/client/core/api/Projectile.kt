package com.runesuite.client.core.api

import com.runesuite.client.core.api.live.Scene
import com.runesuite.client.core.raw.access.XProjectile

class Projectile(override val accessor: XProjectile) : com.runesuite.client.core.api.Entity(accessor), com.runesuite.client.core.api.ActorTargeting {

    val id get() = accessor.id

    val sourcePosition: com.runesuite.client.core.api.Position
        get() = com.runesuite.client.core.api.Position(accessor.sourceX, accessor.sourceY, 0, accessor.plane)
                .let { it.copy(height = Scene.Live.getTileHeight(it) - accessor.sourceZ) }

    val pitch get() = accessor.pitch

    override val orientation get() = com.runesuite.client.core.api.Angle(accessor.yaw)

    override val position: com.runesuite.client.core.api.Position
        get() = com.runesuite.client.core.api.Position(accessor.x.toInt(), accessor.y.toInt(), 0, accessor.plane)
                .let { it.copy(height = Scene.Live.getTileHeight(it) - accessor.z.toInt()) }

    override val model: com.runesuite.client.core.api.Model? get() {
        if (!accessor.isMoving) return null
        return accessor.model?.let { com.runesuite.client.core.api.Model(position, orientation, it) }
    }

    override val npcTargetIndex: Int?
        get() = accessor.targetIndex.let { if (it > 0) it - 1 else null }

    override val playerTargetIndex: Int?
        get() = accessor.targetIndex.let { if (it < 0) -1 * it - 1 else null }

    val speed get() = accessor.speed

    val speedX get() = accessor.speedX

    val speedY get() = accessor.speedY

    val speedZ get() = accessor.speedZ
}
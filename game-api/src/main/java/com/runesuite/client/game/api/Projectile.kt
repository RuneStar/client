package com.runesuite.client.game.api

import com.runesuite.client.game.api.live.Scene
import com.runesuite.client.game.raw.access.XProjectile

class Projectile(override val accessor: XProjectile) : Entity(accessor), ActorTargeting {

    val id get() = accessor.id

    val sourcePosition: Position
        get() = Position(accessor.sourceX, accessor.sourceY, 0, accessor.plane)
                .let { it.copy(height = Scene.Live.getTileHeight(it) - accessor.sourceZ) }

    val pitch get() = accessor.pitch

    override val orientation get() = Angle(accessor.yaw)

    override val position: Position
        get() = Position(accessor.x.toInt(), accessor.y.toInt(), 0, accessor.plane)
                .let { it.copy(height = Scene.Live.getTileHeight(it) - accessor.z.toInt()) }

    override val npcTargetIndex: Int?
        get() = accessor.targetIndex.let { if (it > 0) it - 1 else null }

    override val playerTargetIndex: Int?
        get() = accessor.targetIndex.let { if (it < 0) -1 * it - 1 else null }

    val speed get() = accessor.speed

    val speedX get() = accessor.speedX

    val speedY get() = accessor.speedY

    val speedZ get() = accessor.speedZ
}
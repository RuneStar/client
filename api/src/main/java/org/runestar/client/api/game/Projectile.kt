package org.runestar.client.api.game

import org.runestar.client.api.game.live.Scene
import org.runestar.client.raw.access.XProjectile

class Projectile(override val accessor: XProjectile) : Entity(accessor), ActorTargeting {

    val id get() = accessor.id

    val sourcePosition: Position
        get() = Position(accessor.sourceX, accessor.sourceY, 0, plane)
                .let { it.copy(height = Scene.getTileHeight(it) - accessor.sourceZ) }

    val pitch get() = accessor.pitch

    override val orientation get() = Angle.of(accessor.yaw)

    override val modelPosition: Position
        get() = Position(accessor.x.toInt(), accessor.y.toInt(), 0, plane)
                .let { it.copy(height = Scene.getTileHeight(it) - accessor.z.toInt()) }

    override val npcTargetIndex: Int
        get() = accessor.targetIndex.let { if (it > 0) it - 1 else -1 }

    override val playerTargetIndex: Int
        get() = accessor.targetIndex.let { if (it < 0) -1 * it - 1 else -1 }

    val speed get() = accessor.speed

    val speedX get() = accessor.speedX

    val speedY get() = accessor.speedY

    val speedZ get() = accessor.speedZ

    val plane get() = accessor.plane
}
package com.runesuite.client.core.api

import com.runesuite.client.core.raw.Client
import com.runesuite.client.core.raw.access.XActor

abstract class Actor(override val accessor: XActor) : com.runesuite.client.core.api.Entity(accessor), com.runesuite.client.core.api.ActorTargeting {

    private val plane = Client.accessor.plane

    override val npcTargetIndex: Int?
        get() = accessor.targetIndex.let { if (it in 0..32767) it else null }

    override val playerTargetIndex: Int?
        get() = accessor.targetIndex.let { if (it > 32768) it - 32768 else null }

    override val position get() = com.runesuite.client.core.api.Position(accessor.x, accessor.y, accessor.height, plane)

    val location get() = com.runesuite.client.core.api.SceneTile(accessor.pathX[0], accessor.pathY[0], plane)

    override val orientation get() = com.runesuite.client.core.api.Angle(accessor.orientation)

    val overheadText: String? get() = accessor.overheadMessage

    override val model: com.runesuite.client.core.api.Model?
        get() = accessor.model?.let { com.runesuite.client.core.api.Model(position.copy(height = 0), orientation, it) }
}
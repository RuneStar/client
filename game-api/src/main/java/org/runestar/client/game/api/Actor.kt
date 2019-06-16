package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XActor
import org.runestar.client.game.raw.access.XHeadbar
import org.runestar.client.game.raw.access.XHeadbarUpdate

abstract class Actor(override val accessor: XActor) : Entity(accessor), ActorTargeting {

    abstract val plane: Int

    override val npcTargetIndex: Int
        get() = accessor.targetIndex.let { if (it in 0..32767) it else -1 }

    override val playerTargetIndex: Int
        get() = accessor.targetIndex.let { if (it > 32768) it - 32768 else -1 }

    override val modelPosition get() = Position(accessor.x, accessor.y, 0, plane)

    val location get() = SceneTile(accessor.pathX[0], accessor.pathY[0], plane)

    override val orientation get() = Angle.of(accessor.orientation)

    var overheadText: String?
        get() = accessor.overheadText
        set(value) { accessor.overheadText = value }

    var overheadTextCyclesRemaining: Int
        get() = accessor.overheadTextCyclesRemaining
        set(value) { accessor.overheadTextCyclesRemaining = value }

    /**
     * Health percent between `0.0` and `1.0` of limited precision. `null` if the health-bar is not visible.
     */
    val health: Double? get() {
        val headbars = accessor.headbars ?: return null
        val headbar = headbars.sentinel.next
        if (headbar is XHeadbar) {
            val update = headbar.updates.sentinel.next
            if (update is XHeadbarUpdate) {
                val def = headbar.type ?: return null
                return update.health.toDouble() / def.width
            }
        }
        return null
    }

    val defaultHeight: Int get() = accessor.defaultHeight
}
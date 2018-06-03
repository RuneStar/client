package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XActor
import org.runestar.client.game.raw.access.XHealthBar
import org.runestar.client.game.raw.access.XHealthBarUpdate

abstract class Actor(override val accessor: XActor) : Entity(accessor), ActorTargeting {

    abstract val plane: Int

    override val npcTargetIndex: Int?
        get() = accessor.targetIndex.let { if (it in 0..32767) it else null }

    override val playerTargetIndex: Int?
        get() = accessor.targetIndex.let { if (it > 32768) it - 32768 else null }

    override val modelPosition get() = Position(accessor.x, accessor.y, 0, plane)

    val location get() = SceneTile(accessor.pathX[0], accessor.pathY[0], plane)

    override val orientation get() = Angle.of(accessor.orientation)

    val overheadText: String? get() = accessor.overheadText

    /**
     * Health percent between `0.0` and `1.0` of limited precision. `null` if the health-bar is not visible.
     */
    val health: Double? get() {
        val healthBars = accessor.healthBars ?: return null
        val healthBar = healthBars.sentinel.next
        if (healthBar is XHealthBar) {
            val update = healthBar.updates.sentinel.next
            if (update is XHealthBarUpdate) {
                val def = healthBar.definition ?: return null
                return update.health.toDouble() / def.width
            }
        }
        return null
    }

    val defaultHeight: Int get() = accessor.defaultHeight
}
package org.runestar.client.game.api

import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XActor
import org.runestar.client.game.raw.access.XHealthBar
import org.runestar.client.game.raw.access.XHitSplat

abstract class Actor(override val accessor: XActor) : Entity(accessor), ActorTargeting {

    private val plane = Client.accessor.plane // todo

    override val npcTargetIndex: Int?
        get() = accessor.targetIndex.let { if (it in 0..32767) it else null }

    override val playerTargetIndex: Int?
        get() = accessor.targetIndex.let { if (it > 32768) it - 32768 else null }

    override val position get() = Position(accessor.x, accessor.y, accessor.height, plane)

    val location get() = SceneTile(accessor.pathX[0], accessor.pathY[0], plane)

    override val orientation get() = Angle(accessor.orientation)

    val overheadText: String? get() = accessor.overheadText

    /**
     * Health percent between `0.0` and `1.0` of limited precision. `null` if the health-bar is not visible.
     */
    val health: Double? get() {
        val healthBars = accessor.healthBars ?: return null
        val healthBar = healthBars.sentinel.next
        if (healthBar is XHealthBar) {
            val hitSplat = healthBar.hitSplats.sentinel.next
            if (hitSplat is XHitSplat) {
                val def = healthBar.definition ?: return null
                return hitSplat.health.toDouble() / def.width
            }
        }
        return null
    }
}
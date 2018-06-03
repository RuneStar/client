package org.runestar.client.game.api.live

import org.runestar.client.game.api.Angle
import org.runestar.client.game.api.Minimap
import org.runestar.client.game.api.Scene
import org.runestar.client.game.api.WindowMode
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.Client.accessor
import java.awt.Point
import java.awt.geom.Ellipse2D

object LiveMinimap : Minimap {

    const val RADIUS = 80

    private const val PADDING = 3

    override val reference get() = Players.local?.modelPosition ?: Scene.CENTER.center

    private val FIXED_CENTER = Point(641, RADIUS + PADDING)

    override val center: Point get() = when(Game.windowMode) {
        WindowMode.RESIZABLE -> Point(LiveCanvas.shape.width - (RADIUS + PADDING), (RADIUS + PADDING))
        WindowMode.FIXED -> FIXED_CENTER
    }

    override val zoom get() = 0

    override val orientation get() = Angle.of(accessor.minimapOrientation)

    /**
     * Due to overlapping orbs, this is not the clickable area.
     */
    val circle get() = Ellipse2D.Float().apply { setFrameFromCenter(center, Point(center.x + RADIUS, center.y + RADIUS)) }

    var isDrawn: Boolean
        get() = Client.accessor.minimapState.let { it != 2 && it != 5 }
        set(value) {
            val old = Client.accessor.minimapState
            Client.accessor.minimapState = if (isDrawn) {
                old + 1
            } else {
                old - 1
            }
        }

    val isClickable: Boolean get() = Client.accessor.minimapState < 3

    val isCompassDrawn: Boolean get() = Client.accessor.minimapState.let { it != 2 && it != 5 }

    override fun toString(): String {
        return "LiveMinimap(orientation=$orientation, reference=$reference, center=$center)"
    }
}


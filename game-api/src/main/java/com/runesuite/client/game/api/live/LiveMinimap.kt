package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Angle
import com.runesuite.client.game.api.Minimap
import com.runesuite.client.game.api.Scene
import com.runesuite.client.game.raw.Client.accessor
import java.awt.Point
import java.awt.geom.Ellipse2D

object LiveMinimap : Minimap {

    const val RADIUS = 80

    private const val PADDING = 3

    override val reference get() = Players.local?.position?.copy(height = 0) ?: Scene.CENTER.center

    private val FIXED_CENTER = Point(641, RADIUS + PADDING)

    override val center: Point get() = when(Game.windowMode) {
        Game.WindowMode.RESIZABLE -> Point(LiveCanvas.shape.width - (RADIUS + PADDING), (RADIUS + PADDING))
        Game.WindowMode.FIXED -> FIXED_CENTER
    }

    override val zoom get() = 0

    override val orientation get() = Angle(accessor.minimapOrientation)

    /**
     * Due to overlapping orbs, this is not the clickable area
     */
    val circle get() = Ellipse2D.Float().apply { setFrameFromCenter(center, Point(center.x + RADIUS, center.y + RADIUS)) }

    override fun toString(): String {
        return "LiveMinimap(zoom=$zoom, orientation=$orientation, reference=$reference, center=$center)"
    }
}


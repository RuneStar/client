package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.Angle
import com.runesuite.client.game.Position
import java.awt.Point
import java.awt.geom.Ellipse2D

interface Minimap {

    val scale: Int

    val orientation: Angle

    val reference: Position

    val center: Point

    object Live : Minimap {

        const val RADIUS = 80

        private const val PADDING = 3

        override val reference get() = Players.local?.position?.copy(height = 0) ?: Scene.CENTER.center

        private val FIXED_CENTER = Point(641, RADIUS + PADDING)

        override val center: Point get() = when(Game.windowMode) {
            Game.WindowMode.RESIZABLE -> Point(Canvas.Live.shape.width - (RADIUS + PADDING), (RADIUS + PADDING))
            Game.WindowMode.FIXED -> FIXED_CENTER
        }

        override val scale get() = accessor.minimapScale

        override val orientation get() = baseOrientation + orientationOffset

        val baseOrientation get() = Angle(accessor.minimapOrientation)

        val orientationOffset get() = Angle(accessor.minimapOrientationOffset)

        /**
         * Due to overlapping orbs, this is not the clickable area
         */
        val circle get() = Ellipse2D.Float().apply { setFrameFromCenter(center, Point(center.x + RADIUS, center.y + RADIUS)) }

        override fun toString(): String {
            return "Minimap.Live(scale=$scale, orientation=$orientation, reference=$reference, center=$center)"
        }
    }

    fun copyOf(): Copy = Copy(scale, orientation, reference, center)

    data class Copy(
            override val scale: Int,
            override val orientation: Angle,
            override val reference: Position,
            override val center: Point
    ) : Minimap
}


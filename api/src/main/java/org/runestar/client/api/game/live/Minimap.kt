package org.runestar.client.api.game.live

import org.runestar.client.api.game.Angle
import org.runestar.client.api.game.Position
import org.runestar.client.api.game.Projection
import org.runestar.client.api.game.WindowMode
import org.runestar.client.raw.CLIENT
import java.awt.Point
import java.awt.geom.Ellipse2D

object Minimap : Projection {

    const val RADIUS = 80

    private const val PADDING = 3

    val reference get() = Players.local?.modelPosition ?: Scene.CENTER.center

    private val FIXED_CENTER = Point(641, RADIUS + PADDING)

    val center: Point get() = when(Game.windowMode) {
        WindowMode.RESIZABLE -> Point(Canvas.width - (RADIUS + PADDING), (RADIUS + PADDING))
        WindowMode.FIXED -> FIXED_CENTER
        else -> error(Game.windowMode)
    }

    val zoom get() = 0

    val orientation get() = Angle.of(CLIENT.camAngleY)

    /**
     * Due to overlapping orbs, this is not the clickable area.
     */
    val circle get() = Ellipse2D.Float().apply { setFrameFromCenter(center, Point(center.x + RADIUS, center.y + RADIUS)) }

    var isDrawn: Boolean
        get() = CLIENT.minimapState.let { it != 2 && it != 5 }
        set(value) {
            val old = isDrawn
            if (old == value) return
            if (old) {
                CLIENT.minimapState++
            } else {
                CLIENT.minimapState--
            }
        }

    val isClickable: Boolean get() = CLIENT.minimapState < 3

    val isCompassDrawn: Boolean get() = CLIENT.minimapState.let { it == 0 || it == 3 }

    override fun toScreen(
            localX: Int, localY: Int, height: Int, plane: Int,
            tileHeightLocalX: Int, tileHeightLocalY: Int,
            result: Point
    ): Boolean {
        val minimapReference = reference
        val dx = (localX - minimapReference.localX) shr 5
        val dy = (localY - minimapReference.localY) shr 5
        val minimapZoom = zoom + 256
        val minimapOrientation = orientation
        val sin = minimapOrientation.sinInternal * 256 / minimapZoom
        val cos = minimapOrientation.cosInternal * 256 / minimapZoom
        val x2 = (dy * sin + dx * cos) shr 16
        val y2 = (dy * cos - dx * sin) shr 16
        val minimapCenter = center
        result.x = minimapCenter.x + x2
        result.y = minimapCenter.y - y2
        return true
    }

    override fun toGame(x: Int, y: Int): Position {
        val minimapCenter = center
        val x2 = minimapCenter.x - x
        val y2 = minimapCenter.y - y
        val minimapOrientation = orientation
        val minimapZoom = zoom + 256
        val sin = minimapOrientation.sinInternal * minimapZoom shr 8
        val cos = minimapOrientation.cosInternal * minimapZoom shr 8
        val dx = (y2 * sin + x2 * cos) shr 11
        val dy = (y2 * cos - x2 * sin) shr 11
        return reference.plusLocal(dx * -1, dy).copy(height = 0)
    }

    override fun toString(): String {
        return "Minimap(orientation=$orientation, reference=$reference, center=$center)"
    }
}


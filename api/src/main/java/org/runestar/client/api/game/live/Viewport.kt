package org.runestar.client.api.game.live

import org.runestar.client.api.game.Component
import org.runestar.client.api.game.Position
import org.runestar.client.api.game.Projection
import org.runestar.client.api.game.SceneTile
import org.runestar.client.raw.CLIENT
import java.awt.Point
import java.awt.Rectangle

object Viewport : Projection {

    const val FIXED_ZOOM_DEFAULT = 512

    const val FIXED_ZOOM_MIN = 390

    const val FIXED_ZOOM_MAX = 1400

    val x: Int get() = CLIENT.viewportOffsetX

    val y: Int get() = CLIENT.viewportOffsetY

    val width: Int get() = CLIENT.viewportWidth

    val height: Int get() = CLIENT.viewportHeight

    val zoom: Int get() = CLIENT.viewportZoom

    val shape get() = Rectangle(x, y, width, height)

    fun contains(x: Int, y: Int) = x >= this.x && y >= this.y && x <= (this.x + width) && y <= (this.y + height)

    operator fun contains(point: Point) = contains(point.x, point.y)

    val component: Component? get() = CLIENT.viewportComponent?.let { Component(it) }

    override fun toScreen(
            localX: Int, localY: Int, height: Int, plane: Int,
            tileHeightLocalX: Int, tileHeightLocalY: Int,
            result: Point
    ): Boolean {
        if (!Position.isLoaded(tileHeightLocalX, tileHeightLocalY, plane)) return false
        var x1 = localX
        var y1 = localY
        var z1 = Scene.getTileHeight(tileHeightLocalX, tileHeightLocalY, plane) - height
        x1 -= Camera.localX
        y1 -= Camera.localY
        z1 -= Camera.absoluteHeight
        val cameraPitch = Camera.pitch
        val sinY = cameraPitch.sinInternal
        val cosY = cameraPitch.cosInternal
        val cameraYaw = Camera.yaw
        val sinX = cameraYaw.sinInternal
        val cosX = cameraYaw.cosInternal
        val x2 = (y1 * sinX + x1 * cosX) shr 16
        y1 = (y1 * cosX - sinX * x1) shr 16
        x1 = x2
        val z2 = (cosY * z1 - y1 * sinY) shr 16
        y1 = (z1 * sinY + y1 * cosY) shr 16
        z1 = z2
        if (y1 < 50) {
            return false
        }
        result.x = this.width / 2 + x1 * zoom / y1 + x
        result.y = this.height / 2 + z1 * zoom / y1 + y
        return true
    }

    override fun toGame(x: Int, y: Int): Position? {
        // todo
        val plane = Camera.plane
        for (xi in 0 until Scene.SIZE) {
            for (yi in 0 until Scene.SIZE) {
                val tile = SceneTile(xi, yi, plane)
                val bounds = tile.outline(this)
                if (bounds.contains(x.toDouble(), y.toDouble())) {
                    return tile.center
                }
            }
        }
        return null
    }

    override fun toString(): String {
        return "Viewport(zoom=$zoom, shape=$shape)"
    }
}
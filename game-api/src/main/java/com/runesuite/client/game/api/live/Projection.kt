package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Position
import java.awt.Point

interface Projection {

    fun toGame(point: Point): com.runesuite.client.game.api.Position

    fun toScreen(position: com.runesuite.client.game.api.Position): Point {
        return toScreen(position, position)
    }

    fun toScreen(position: com.runesuite.client.game.api.Position, tileHeight: com.runesuite.client.game.api.Position): Point

    open class Minimap(val minimap: com.runesuite.client.game.api.live.Minimap) : Projection {

        override fun toScreen(position: com.runesuite.client.game.api.Position, tileHeight: com.runesuite.client.game.api.Position): Point {
            val dx = (position.localX - minimap.reference.localX) shr 5
            val dy = (position.localY - minimap.reference.localY) shr 5
            val sin = minimap.orientation.sinInternal * 256 / (minimap.scale + 256)
            val cos = minimap.orientation.cosInternal * 256 / (minimap.scale + 256)
            val x2 = (dy * sin + dx * cos) shr 16
            val y2 = (dy * cos - dx * sin) shr 16
            return Point(minimap.center.x + x2, minimap.center.y - y2)
        }

        override fun toGame(point: Point): com.runesuite.client.game.api.Position {
            val x2 = minimap.center.x - point.x
            val y2 = minimap.center.y + point.y * -1
            val sin = minimap.orientation.sinInternal * (minimap.scale + 256) shr 8
            val cos = minimap.orientation.cosInternal * (minimap.scale + 256) shr 8
            val dx = (y2 * sin + x2 * cos) shr 11
            val dy = (y2 * cos - x2 * sin) shr 11
            return minimap.reference.plusLocal(dx * -1, dy)
        }

        object Live : Minimap(com.runesuite.client.game.api.live.Minimap.Live)
    }

    open class Viewport(val camera: Camera, val viewport: com.runesuite.client.game.api.live.Viewport, val scene: Scene) : Projection {

        override fun toScreen(position: com.runesuite.client.game.api.Position, tileHeight: com.runesuite.client.game.api.Position): Point {
            require(tileHeight.isLoaded) { tileHeight }
            var x1 = position.localX
            var y1 = position.localY
            var z1 = scene.getTileHeight(tileHeight) - position.height
            x1 -= camera.x
            y1 -= camera.y
            z1 -= camera.z
            val sinY = camera.pitch.sinInternal
            val cosY = camera.pitch.cosInternal
            val sinX = camera.yaw.sinInternal
            val cosX = camera.yaw.cosInternal
            val x2 = (y1 * sinX + x1 * cosX) shr 16
            y1 = (y1 * cosX - sinX * x1) shr 16
            x1 = x2
            val z2 = (cosY * z1 - y1 * sinY) shr 16
            y1 = (z1 * sinY + y1 * cosY) shr 16
            z1 = z2
            if (y1 == 0) {
                y1 = 1
            }
            return Point(viewport.width / 2 + x1 * viewport.scale / y1 + viewport.x,
                    viewport.height / 2 + z1 * viewport.scale / y1 + viewport.y)
        }

        override fun toGame(point: Point): com.runesuite.client.game.api.Position {
            TODO("not implemented")
        }

        object Live : Viewport(Camera.Live, com.runesuite.client.game.api.live.Viewport.Live, Scene.Live)
    }
}
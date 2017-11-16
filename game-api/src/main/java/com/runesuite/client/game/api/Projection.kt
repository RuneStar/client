package com.runesuite.client.game.api

import java.awt.Point

interface Projection {

    fun toGame(point: Point): Position

    fun toScreen(position: Position): Point {
        return toScreen(position, position)
    }

    fun toScreen(position: Position, tileHeight: Position): Point

    data class Minimap(val minimap: com.runesuite.client.game.api.Minimap) : Projection {

        override fun toScreen(position: Position, tileHeight: Position): Point {
            val minimapCopy = minimap.copyOf()
            val dx = (position.localX - minimapCopy.reference.localX) shr 5
            val dy = (position.localY - minimapCopy.reference.localY) shr 5
            val sin = minimapCopy.orientation.sinInternal * 256 / (minimapCopy.zoom + 256)
            val cos = minimapCopy.orientation.cosInternal * 256 / (minimapCopy.zoom + 256)
            val x2 = (dy * sin + dx * cos) shr 16
            val y2 = (dy * cos - dx * sin) shr 16
            return Point(minimapCopy.center.x + x2, minimapCopy.center.y - y2)
        }

        override fun toGame(point: Point): Position {
            val minimapCopy = minimap.copyOf()
            val x2 = minimapCopy.center.x - point.x
            val y2 = minimapCopy.center.y - point.y
            val sin = minimapCopy.orientation.sinInternal * (minimapCopy.zoom + 256) shr 8
            val cos = minimapCopy.orientation.cosInternal * (minimapCopy.zoom + 256) shr 8
            val dx = (y2 * sin + x2 * cos) shr 11
            val dy = (y2 * cos - x2 * sin) shr 11
            return minimapCopy.reference.plusLocal(dx * -1, dy).copy(height = 0)
        }
    }

    data class Viewport(
            val camera: Camera,
            val viewport: com.runesuite.client.game.api.Viewport,
            val scene: Scene
    ) : Projection {

        override fun toScreen(position: Position, tileHeight: Position): Point {
            require(tileHeight.isLoaded) { tileHeight }
            var x1 = position.localX
            var y1 = position.localY
            var z1 = scene.getTileHeight(tileHeight) - position.height
            val cameraCopy = camera.copyOf()
            x1 -= cameraCopy.position.localX
            y1 -= cameraCopy.position.localY
            z1 -= scene.getTileHeight(cameraCopy.position) - cameraCopy.position.height
            val sinY = cameraCopy.pitch.sinInternal
            val cosY = cameraCopy.pitch.cosInternal
            val sinX = cameraCopy.yaw.sinInternal
            val cosX = cameraCopy.yaw.cosInternal
            val x2 = (y1 * sinX + x1 * cosX) shr 16
            y1 = (y1 * cosX - sinX * x1) shr 16
            x1 = x2
            val z2 = (cosY * z1 - y1 * sinY) shr 16
            y1 = (z1 * sinY + y1 * cosY) shr 16
            z1 = z2
            if (y1 < 50) {
                return Point(-1, -1) // todo
            }
            val viewportCopy = viewport.copyOf()
            return Point(
                    viewportCopy.width / 2 + x1 * viewportCopy.zoom / y1 + viewportCopy.x,
                    viewportCopy.height / 2 + z1 * viewportCopy.zoom / y1 + viewportCopy.y
            )
        }

        override fun toGame(point: Point): Position {
            TODO("not implemented")
        }
    }
}
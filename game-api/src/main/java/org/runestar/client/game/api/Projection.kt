package org.runestar.client.game.api

import java.awt.Point

interface Projection {

    fun toGame(point: Point): Position?

    fun toScreen(position: Position): Point? {
        return toScreen(position, position)
    }

    fun toScreen(position: Position, tileHeight: Position): Point?

    data class Minimap(
            val minimap: org.runestar.client.game.api.Minimap
    ) : Projection {

        override fun toScreen(position: Position, tileHeight: Position): Point {
            val minimapReference = minimap.reference
            val dx = (position.localX - minimapReference.localX) shr 5
            val dy = (position.localY - minimapReference.localY) shr 5
            val minimapZoom = minimap.zoom + 256
            val minimapOrientation = minimap.orientation
            val sin = minimapOrientation.sinInternal * 256 / minimapZoom
            val cos = minimapOrientation.cosInternal * 256 / minimapZoom
            val x2 = (dy * sin + dx * cos) shr 16
            val y2 = (dy * cos - dx * sin) shr 16
            val minimapCenter = minimap.center
            return Point(minimapCenter.x + x2, minimapCenter.y - y2)
        }

        override fun toGame(point: Point): Position {
            val minimapCenter = minimap.center
            val x2 = minimapCenter.x - point.x
            val y2 = minimapCenter.y - point.y
            val minimapOrientation = minimap.orientation
            val minimapZoom = minimap.zoom + 256
            val sin = minimapOrientation.sinInternal * minimapZoom shr 8
            val cos = minimapOrientation.cosInternal * minimapZoom shr 8
            val dx = (y2 * sin + x2 * cos) shr 11
            val dy = (y2 * cos - x2 * sin) shr 11
            return minimap.reference.plusLocal(dx * -1, dy).copy(height = 0)
        }

        override fun toScreen(position: Position): Point {
            return toScreen(position, position)
        }
    }

    data class Viewport(
            val camera: Camera,
            val viewport: org.runestar.client.game.api.Viewport,
            val scene: Scene
    ) : Projection {

        override fun toScreen(position: Position, tileHeight: Position): Point? {
            require(tileHeight.isLoaded) { tileHeight }
            var x1 = position.localX
            var y1 = position.localY
            var z1 = scene.getTileHeight(tileHeight) - position.height
            val cameraPosition = camera.position
            x1 -= cameraPosition.localX
            y1 -= cameraPosition.localY
            z1 -= scene.getTileHeight(cameraPosition) - cameraPosition.height
            val cameraPitch = camera.pitch
            val sinY = cameraPitch.sinInternal
            val cosY = cameraPitch.cosInternal
            val cameraYaw = camera.yaw
            val sinX = cameraYaw.sinInternal
            val cosX = cameraYaw.cosInternal
            val x2 = (y1 * sinX + x1 * cosX) shr 16
            y1 = (y1 * cosX - sinX * x1) shr 16
            x1 = x2
            val z2 = (cosY * z1 - y1 * sinY) shr 16
            y1 = (z1 * sinY + y1 * cosY) shr 16
            z1 = z2
            if (y1 < 50) {
                return null
            }
            val viewportZoom = viewport.zoom
            return Point(
                    viewport.width / 2 + x1 * viewportZoom / y1 + viewport.x,
                    viewport.height / 2 + z1 * viewportZoom / y1 + viewport.y
            )
        }

        override fun toGame(point: Point): Position? {
            // todo
            val plane = camera.position.plane
            for (x in 0 until Scene.SIZE) {
                for (y in 0 until Scene.SIZE) {
                    val tile = SceneTile(x, y, plane)
                    val bounds = tile.outline(this)
                    if (point in bounds) {
                        return tile.center
                    }
                }
            }
            return null
        }
    }
}
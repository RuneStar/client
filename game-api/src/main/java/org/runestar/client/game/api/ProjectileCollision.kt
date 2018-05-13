package org.runestar.client.game.api

import kotlin.math.sign

data class ProjectileCollision(val scene: Scene) {

    fun canReach(source: SceneTile, destination: SceneTile): Boolean {
        if (source == destination) return false
        require(source.plane == destination.plane)
        val plane = source.plane
        val dx = destination.x - source.x
        val dy = destination.y - source.y
        val dxAbs = Math.abs(dx)
        val dyAbs = Math.abs(dy)

        val xFlags = CollisionFlag.PROJECTILE_OBJECT or if (dx < 0) {
            CollisionFlag.PROJECTILE_EAST
        } else {
            CollisionFlag.PROJECTILE_WEST
        }
        val yFlags = CollisionFlag.PROJECTILE_OBJECT or if (dy < 0) {
            CollisionFlag.PROJECTILE_NORTH
        } else {
            CollisionFlag.PROJECTILE_SOUTH
        }

        var x = source.x
        var y = source.y

        // todo
        if (dxAbs > dyAbs) {
            var yExact = (y shl 16) + (1 shl 15)
            if (dy < 0) {
                yExact--
            }
            val yStep = (dy shl 16) / dxAbs
            val xStep = dx.sign
            while (x != destination.x) {
                x += xStep
                if ((scene.getCollisionFlags(x, y, plane) and xFlags) != 0) return false
                yExact += yStep
                val nextY = yExact ushr 16
                if (nextY != y && (scene.getCollisionFlags(x, nextY, plane) and yFlags) != 0) return false
                y = nextY
            }
        } else {
            var xExact = (x shl 16) + (1 shl 15)
            if (dx < 0) {
                xExact--
            }
            val xStep = (dx shl 16) / dyAbs
            val yStep = dy.sign
            while (y != destination.y) {
                y += yStep
                if ((scene.getCollisionFlags(x, y, plane) and yFlags) != 0) return false
                xExact += xStep
                val nextX = xExact ushr 16
                if (nextX != x && (scene.getCollisionFlags(nextX, y, plane) and xFlags) != 0) return false
                x = nextX
            }
        }
        return true
    }
}
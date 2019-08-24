package org.runestar.client.game.api.live

import org.runestar.client.game.api.CollisionFlag
import org.runestar.client.game.api.OctantDirection
import org.runestar.client.game.api.SceneTile

object Movement {

    private const val ALL = CollisionFlag.WALK_OBJECT or CollisionFlag.WALK_GROUND
    private const val N = ALL or CollisionFlag.WALK_NORTH
    private const val E = ALL or CollisionFlag.WALK_EAST
    private const val S = ALL or CollisionFlag.WALK_SOUTH
    private const val W = ALL or CollisionFlag.WALK_WEST
    private const val NE = ALL or CollisionFlag.WALK_NORTH or CollisionFlag.WALK_NORTHEAST or CollisionFlag.WALK_EAST
    private const val SE = ALL or CollisionFlag.WALK_SOUTH or CollisionFlag.WALK_SOUTHEAST or CollisionFlag.WALK_EAST
    private const val SW = ALL or CollisionFlag.WALK_SOUTH or CollisionFlag.WALK_SOUTHWEST or CollisionFlag.WALK_WEST
    private const val NW = ALL or CollisionFlag.WALK_NORTH or CollisionFlag.WALK_NORTHWEST or CollisionFlag.WALK_WEST

    fun canMove(source: SceneTile, direction: OctantDirection): Boolean {
        return canMove(source.x, source.y, source.plane, direction)
    }

    fun canMove(x: Int, y: Int, plane: Int, direction: OctantDirection): Boolean {
        val destX = x + direction.x
        val destY = y + direction.y
        if (!SceneTile.isXyLoaded(destX) || !SceneTile.isXyLoaded(destY)) return false
        if (Scene.getCollisionFlags(destX, destY, plane) and ALL != 0) return false
        return when (direction) {
            OctantDirection.NORTH -> {
                Scene.getCollisionFlags(x, y, plane) and N == 0
            }
            OctantDirection.NORTHEAST -> {
                Scene.getCollisionFlags(x, y, plane) and NE == 0 &&
                        Scene.getCollisionFlags(destX, y, plane) and N == 0 &&
                        Scene.getCollisionFlags(x, destY, plane) and E == 0
            }
            OctantDirection.EAST -> {
                Scene.getCollisionFlags(x, y, plane) and E == 0
            }
            OctantDirection.SOUTHEAST -> {
                Scene.getCollisionFlags(x, y, plane) and SE == 0 &&
                        Scene.getCollisionFlags(destX, y, plane) and S == 0 &&
                        Scene.getCollisionFlags(x, destY, plane) and E == 0
            }
            OctantDirection.SOUTH -> {
                Scene.getCollisionFlags(x, y, plane) and S == 0
            }
            OctantDirection.SOUTHWEST -> {
                Scene.getCollisionFlags(x, y, plane) and SW == 0 &&
                        Scene.getCollisionFlags(destX, y, plane) and S == 0 &&
                        Scene.getCollisionFlags(x, destY, plane) and W == 0
            }
            OctantDirection.WEST -> {
                Scene.getCollisionFlags(x, y, plane) and W == 0
            }
            OctantDirection.NORTHWEST -> {
                Scene.getCollisionFlags(x, y, plane) and NW == 0 &&
                        Scene.getCollisionFlags(destX, y, plane) and N == 0 &&
                        Scene.getCollisionFlags(x, destY, plane) and W == 0
            }
        }
    }
}
package org.runestar.client.game.api

interface Movement {

    fun canMove(source: SceneTile, direction: OctantDirection): Boolean {
        return canMove(source.x, source.y, source.plane, direction)
    }

    fun canMove(x: Int, y: Int, plane: Int, direction: OctantDirection): Boolean

    data class Walking(val scene: Scene): Movement {

        private companion object {
            val ALL = CollisionFlag.WALK_OBJECT or CollisionFlag.WALK_GROUND
            val N = ALL or CollisionFlag.WALK_NORTH
            val E = ALL or CollisionFlag.WALK_EAST
            val S = ALL or CollisionFlag.WALK_SOUTH
            val W = ALL or CollisionFlag.WALK_WEST
            val NE = ALL or CollisionFlag.WALK_NORTH or CollisionFlag.WALK_NORTHEAST or CollisionFlag.WALK_EAST
            val SE = ALL or CollisionFlag.WALK_SOUTH or CollisionFlag.WALK_SOUTHEAST or CollisionFlag.WALK_EAST
            val SW = ALL or CollisionFlag.WALK_SOUTH or CollisionFlag.WALK_SOUTHWEST or CollisionFlag.WALK_WEST
            val NW = ALL or CollisionFlag.WALK_NORTH or CollisionFlag.WALK_NORTHWEST or CollisionFlag.WALK_WEST
        }

        override fun canMove(x: Int, y: Int, plane: Int, direction: OctantDirection): Boolean {
            val destX = x + direction.x
            val destY = y + direction.y
            if (!SceneTile.isXyLoaded(destX) || !SceneTile.isXyLoaded(destY)) return false
            if (scene.getCollisionFlags(destX, destY, plane) and ALL != 0) return false
            return when (direction) {
                OctantDirection.NORTH -> {
                    scene.getCollisionFlags(x, y, plane) and N == 0
                }
                OctantDirection.NORTHEAST -> {
                    scene.getCollisionFlags(x, y, plane) and NE == 0 &&
                            scene.getCollisionFlags(destX, y, plane) and N == 0 &&
                            scene.getCollisionFlags(x, destY, plane) and E == 0
                }
                OctantDirection.EAST -> {
                    scene.getCollisionFlags(x, y, plane) and E == 0
                }
                OctantDirection.SOUTHEAST -> {
                    scene.getCollisionFlags(x, y, plane) and SE == 0 &&
                            scene.getCollisionFlags(destX, y, plane) and S == 0 &&
                            scene.getCollisionFlags(x, destY, plane) and E == 0
                }
                OctantDirection.SOUTH -> {
                    scene.getCollisionFlags(x, y, plane) and S == 0
                }
                OctantDirection.SOUTHWEST -> {
                    scene.getCollisionFlags(x, y, plane) and SW == 0 &&
                            scene.getCollisionFlags(destX, y, plane) and S == 0 &&
                            scene.getCollisionFlags(x, destY, plane) and W == 0
                }
                OctantDirection.WEST -> {
                    scene.getCollisionFlags(x, y, plane) and W == 0
                }
                OctantDirection.NORTHWEST -> {
                    scene.getCollisionFlags(x, y, plane) and NW == 0 &&
                            scene.getCollisionFlags(destX, y, plane) and N == 0 &&
                            scene.getCollisionFlags(x, destY, plane) and W == 0
                }
            }
        }
    }
}
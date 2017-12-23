package org.runestar.client.game.api

import com.hunterwb.gridnav.*

interface PathFinder {

    fun findPath(source: SceneTile, destination: SceneTile): List<SceneTile>?

    data class Walking(val scene: Scene) : PathFinder {

        // todo :
        // tie-breaking is wrong in some cases, such as standing at the corner of a square object and walking
        // to the opposite corner.
        override fun findPath(source: SceneTile, destination: SceneTile): List<SceneTile>? {
            if (source.plane != destination.plane) return null
            if (source == destination) return emptyList()
            val movement = WalkMovement(scene, source.plane)
            val s = Cell(source.x, source.y)
            val d = Cell(destination.x, destination.y)
            // find path in reverse so that diagonal moves are prioritized last
            val jp = JumpPoint(movement, Distance.Octile.CHEBYSHEV, Distance.Octile.CHEBYSHEV, d)
            val cellPath = jp.findPath(s) ?: return null
            val path = ArrayList<SceneTile>(cellPath.size)
            for (i in (cellPath.lastIndex - 1).downTo(0)) {
                val c = cellPath[i]
                path.add(SceneTile(c.x, c.y, source.plane))
            }
            path.add(destination)
            return path
        }
    }

    data class Projectile(val scene: Scene) : PathFinder {

        override fun findPath(source: SceneTile, destination: SceneTile): List<SceneTile>? {
            // todo : npcs, objects
            if (source.plane != destination.plane) return null
            if (source == destination) return emptyList()
            val s = Cell(source.x, source.y)
            val d = Cell(destination.x, destination.y)
            val movement = ProjectileMovement(scene, source.plane)
            val bl = BresenhamLine(movement, s)
            return bl.findPath(d)?.map { SceneTile(it.x, it.y, source.plane) }
        }
    }

    private class WalkMovement(val scene: Scene, val plane: Int) : Movement {

        init {
            require(plane in 0 until Scene.PLANE_SIZE)
        }

        private companion object {
            val ALL = CollisionFlag.OBJECT_ALL.mask or CollisionFlag.GROUND_ALL.mask
            val N = ALL or CollisionFlag.WALK_NORTH.mask
            val E = ALL or CollisionFlag.WALK_EAST.mask
            val S = ALL or CollisionFlag.WALK_SOUTH.mask
            val W = ALL or CollisionFlag.WALK_WEST.mask
            val NE = ALL or CollisionFlag.WALK_NORTH.mask or CollisionFlag.WALK_NORTHEAST.mask or CollisionFlag.WALK_EAST.mask
            val SE = ALL or CollisionFlag.WALK_SOUTH.mask or CollisionFlag.WALK_SOUTHEAST.mask or CollisionFlag.WALK_EAST.mask
            val SW = ALL or CollisionFlag.WALK_SOUTH.mask or CollisionFlag.WALK_SOUTHWEST.mask or CollisionFlag.WALK_WEST.mask
            val NW = ALL or CollisionFlag.WALK_NORTH.mask or CollisionFlag.WALK_NORTHWEST.mask or CollisionFlag.WALK_WEST.mask
        }

        override fun canMove(source: Cell, direction: OctantDirection): Boolean {
            val flags = scene.getCollisionFlags(plane)
            val dest = source + direction
            if (!SceneTile(dest.x, dest.y, plane).isLoaded) return false
            val isDestOpen = flags[dest.x][dest.y] and ALL == 0
            return isDestOpen && when (direction) {
                OctantDirection.NORTH -> {
                    flags[source.x][source.y] and N == 0
                }
                OctantDirection.NORTHEAST -> {
                    flags[source.x][source.y] and NE == 0 &&
                            flags[dest.x][source.y] and N == 0 &&
                            flags[source.x][dest.y] and E == 0
                }
                OctantDirection.EAST -> {
                    flags[source.x][source.y] and E == 0
                }
                OctantDirection.SOUTHEAST -> {
                    flags[source.x][source.y] and SE == 0 &&
                            flags[dest.x][source.y] and S == 0 &&
                            flags[source.x][dest.y] and E == 0
                }
                OctantDirection.SOUTH -> {
                    flags[source.x][source.y] and S == 0
                }
                OctantDirection.SOUTHWEST -> {
                    flags[source.x][source.y] and SW == 0 &&
                            flags[dest.x][source.y] and S == 0 &&
                            flags[source.x][dest.y] and W == 0
                }
                OctantDirection.WEST -> {
                    flags[source.x][source.y] and W == 0
                }
                OctantDirection.NORTHWEST -> {
                    flags[source.x][source.y] and NW == 0 &&
                            flags[dest.x][source.y] and N == 0 &&
                            flags[source.x][dest.y] and W == 0
                }
            }
        }
    }

    private class ProjectileMovement(val scene: Scene, val plane: Int) : Movement {

        init {
            require(plane in 0 until Scene.PLANE_SIZE)
        }

        private companion object {
            val ALL = 0
            val N = ALL or CollisionFlag.PROJECTILE_NORTH.mask
            val E = ALL or CollisionFlag.PROJECTILE_EAST.mask
            val S = ALL or CollisionFlag.PROJECTILE_SOUTH.mask
            val W = ALL or CollisionFlag.PROJECTILE_WEST.mask
            val NE = ALL or CollisionFlag.PROJECTILE_NORTH.mask or CollisionFlag.PROJECTILE_NORTHEAST.mask or
                    CollisionFlag.PROJECTILE_EAST.mask
            val SE = ALL or CollisionFlag.PROJECTILE_SOUTH.mask or CollisionFlag.PROJECTILE_SOUTHEAST.mask or
                    CollisionFlag.PROJECTILE_EAST.mask
            val SW = ALL or CollisionFlag.PROJECTILE_SOUTH.mask or CollisionFlag.PROJECTILE_SOUTHWEST.mask or
                    CollisionFlag.PROJECTILE_WEST.mask
            val NW = ALL or CollisionFlag.PROJECTILE_NORTH.mask or CollisionFlag.PROJECTILE_NORTHWEST.mask or
                    CollisionFlag.PROJECTILE_WEST.mask
        }

        override fun canMove(source: Cell, direction: OctantDirection): Boolean {
            val flags = scene.getCollisionFlags(plane)
            val dest = source + direction
            if (!SceneTile(dest.x, dest.y, plane).isLoaded) return false
            val isDestOpen = flags[dest.x][dest.y] and ALL == 0
            return isDestOpen && when (direction) {
                OctantDirection.NORTH -> {
                    flags[source.x][source.y] and N == 0
                }
                OctantDirection.NORTHEAST -> {
                    flags[source.x][source.y] and NE == 0 &&
                            flags[dest.x][source.y] and N == 0 &&
                            flags[source.x][dest.y] and E == 0
                }
                OctantDirection.EAST -> {
                    flags[source.x][source.y] and E == 0
                }
                OctantDirection.SOUTHEAST -> {
                    flags[source.x][source.y] and SE == 0 &&
                            flags[dest.x][source.y] and S == 0 &&
                            flags[source.x][dest.y] and E == 0
                }
                OctantDirection.SOUTH -> {
                    flags[source.x][source.y] and S == 0
                }
                OctantDirection.SOUTHWEST -> {
                    flags[source.x][source.y] and SW == 0 &&
                            flags[dest.x][source.y] and S == 0 &&
                            flags[source.x][dest.y] and W == 0
                }
                OctantDirection.WEST -> {
                    flags[source.x][source.y] and W == 0
                }
                OctantDirection.NORTHWEST -> {
                    flags[source.x][source.y] and NW == 0 &&
                            flags[dest.x][source.y] and N == 0 &&
                            flags[source.x][dest.y] and W == 0
                }
            }
        }
    }
}
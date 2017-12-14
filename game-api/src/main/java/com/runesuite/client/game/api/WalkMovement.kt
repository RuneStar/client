package com.runesuite.client.game.api

import com.hunterwb.gridnav.Cell
import com.hunterwb.gridnav.Movement
import com.hunterwb.gridnav.OctantDirection
import com.runesuite.client.game.api.CollisionFlag.*

class WalkMovement(val scene: Scene, val plane: Int) : Movement {

    override fun canMove(source: Cell, direction: OctantDirection): Boolean {
        val flags = scene.getCollisionFlags(plane)
        val dest = source + direction.asCell
        return when (direction) {
            OctantDirection.NORTH -> {
                flags[source.x][source.y] and WALK_NORTH.mask == 0 &&
                        flags[dest.x][dest.y] and ALL.mask == 0
            }
            OctantDirection.NORTHEAST -> {
                flags[source.x][source.y] and (WALK_NORTH.mask or WALK_NORTHEAST.mask or WALK_EAST.mask) == 0 &&
                        flags[dest.x][source.y] and (ALL.mask or WALK_NORTH.mask) == 0 &&
                        flags[dest.x][dest.y] and ALL.mask == 0 &&
                        flags[source.x][dest.y] and (ALL.mask or WALK_EAST.mask) == 0
            }
            OctantDirection.EAST -> {
                flags[source.x][source.y] and WALK_EAST.mask == 0 &&
                        flags[dest.x][dest.y] and ALL.mask == 0
            }
            OctantDirection.SOUTHEAST -> {
                flags[source.x][source.y] and (WALK_SOUTH.mask or WALK_SOUTHEAST.mask or WALK_EAST.mask) == 0 &&
                        flags[dest.x][source.y] and (ALL.mask or WALK_SOUTH.mask) == 0 &&
                        flags[dest.x][dest.y] and ALL.mask == 0 &&
                        flags[source.x][dest.y] and (ALL.mask or WALK_EAST.mask) == 0
            }
            OctantDirection.SOUTH -> {
                flags[source.x][source.y] and WALK_SOUTH.mask == 0 &&
                        flags[dest.x][dest.y] and ALL.mask == 0
            }
            OctantDirection.SOUTHWEST -> {
                flags[source.x][source.y] and (WALK_SOUTH.mask or WALK_SOUTHWEST.mask or WALK_WEST.mask) == 0 &&
                        flags[dest.x][source.y] and (ALL.mask or WALK_SOUTH.mask) == 0 &&
                        flags[dest.x][dest.y] and ALL.mask == 0 &&
                        flags[source.x][dest.y] and (ALL.mask or WALK_WEST.mask) == 0
            }
            OctantDirection.WEST -> {
                flags[source.x][source.y] and WALK_WEST.mask == 0 &&
                        flags[dest.x][dest.y] and ALL.mask == 0
            }
            OctantDirection.NORTHWEST -> {
                flags[source.x][source.y] and (WALK_NORTH.mask or WALK_SOUTHEAST.mask or WALK_WEST.mask) == 0 &&
                        flags[dest.x][source.y] and (ALL.mask or WALK_NORTH.mask) == 0 &&
                        flags[dest.x][dest.y] and ALL.mask == 0 &&
                        flags[source.x][dest.y] and (ALL.mask or WALK_WEST.mask) == 0
            }
        }
    }
}
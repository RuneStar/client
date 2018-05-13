package org.runestar.client.game.api

object CollisionFlag {

    const val WALK_NORTHWEST =  1
    const val WALK_NORTH =      1 shl 1
    const val WALK_NORTHEAST =  1 shl 2
    const val WALK_EAST =       1 shl 3
    const val WALK_SOUTHEAST =  1 shl 4
    const val WALK_SOUTH =      1 shl 5
    const val WALK_SOUTHWEST =  1 shl 6
    const val WALK_WEST =       1 shl 7
    const val WALK_OBJECT =     1 shl 8
    const val PROJECTILE_NORTHWEST =    1 shl 9
    const val PROJECTILE_NORTH =        1 shl 10
    const val PROJECTILE_NORTHEAST =    1 shl 11
    const val PROJECTILE_EAST =         1 shl 12
    const val PROJECTILE_SOUTHEAST =    1 shl 13
    const val PROJECTILE_SOUTH =        1 shl 14
    const val PROJECTILE_SOUTHWEST =    1 shl 15
    const val PROJECTILE_WEST =         1 shl 16
    const val PROJECTILE_OBJECT =       1 shl 17

    const val WALK_GROUND = 1 shl 21
}
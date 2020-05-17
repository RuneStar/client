package org.runestar.client.api.game

enum class OctantDirection(
        val x: Int,
        val y: Int
) {

    NORTH(0, 1),
    NORTHEAST(1, 1),
    EAST(1, 0),
    SOUTHEAST(1, -1),
    SOUTH(0, -1),
    SOUTHWEST(-1, -1),
    WEST(-1, 0),
    NORTHWEST(-1, 1);

    companion object {

        @JvmField val VALUES = values().asList()

        fun of(x: Int, y: Int): OctantDirection {
            return when (x) {
                -1 -> when (y) {
                    -1 -> SOUTHWEST
                    0 -> WEST
                    1 -> NORTHWEST
                    else -> throw IllegalArgumentException()
                }
                0 -> when (y) {
                    -1 -> SOUTH
                    1 -> NORTH
                    else -> throw IllegalArgumentException()
                }
                1 -> when (y) {
                    -1 -> SOUTHEAST
                    0 -> EAST
                    1 -> NORTHEAST
                    else -> throw IllegalArgumentException()
                }
                else -> throw IllegalArgumentException()
            }
        }
    }
}
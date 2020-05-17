package org.runestar.client.api.game

inline class ChunkTemplate(val packed: Int) {

    val rotation: Int get() = (packed shr 1) and 0x3

    val y: Int get() = (packed shr 3) and 0x7FF

    val x: Int get() = (packed shr 14) and 0x3FF

    val plane: Int get() = (packed shr 24) and 0x3

    override fun toString() = "ChunkTemplate(x=$x, y=$y, plane=$plane, rotation=$rotation)"

    companion object {

        const val CHUNK_SIZE = 8

        fun rotateX(
                chunkX: Int,
                chunkY: Int,
                rotation: Int
        ): Int {
            return when (rotation) {
                0 -> chunkX
                1 -> chunkY
                2 -> CHUNK_SIZE - 1 - chunkX
                3 -> CHUNK_SIZE - 1 - chunkY
                else -> error(rotation)
            }
        }

        fun rotateY(
                chunkX: Int,
                chunkY: Int,
                rotation: Int
        ): Int {
            return when (rotation) {
                0 -> chunkY
                1 -> CHUNK_SIZE - 1 - chunkX
                2 -> CHUNK_SIZE - 1 - chunkY
                3 -> chunkX
                else -> error(rotation)
            }
        }
    }
}
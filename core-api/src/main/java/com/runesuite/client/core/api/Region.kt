package com.runesuite.client.core.api

data class Region(val x: Int, val y: Int, val plane: Int) {

    internal constructor(packed: Int) : this(packed shr 14 and 255, packed and 255 , packed shr 28)

    val base get() = GlobalTile(x * Region.SIZE, y * Region.SIZE, plane)

    val corners get() = base.run { listOf(
            this,
            copy(x = x + Region.SIZE - 1),
            copy(x = x + Region.SIZE - 1, y = y + Region.SIZE - 1),
            copy(y = y + Region.SIZE - 1)) }

    internal val packed get() = x shl 14 or y or plane shl 28

    companion object {
        const val SIZE = 64
    }
}

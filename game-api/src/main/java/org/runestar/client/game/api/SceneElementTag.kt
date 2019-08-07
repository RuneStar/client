package org.runestar.client.game.api

inline class SceneElementTag(val packed: Long) {

    val kind: Int get() = ((packed shr 14) and 0x3).toInt()

    val id: Int get() = (packed ushr 17).toInt()

    val x: Int get() = (packed and 0x7F).toInt()

    val y: Int get() = ((packed shr 7) and 0x7F).toInt()

    val interactable: Boolean get() = (packed and 0x10000) == 0L

    override fun toString(): String {
        return "SceneElementTag(kind=$kind, id=$id, x=$x, y=$y, interactable=$interactable)"
    }
}
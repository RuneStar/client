package org.runestar.client.game.api

data class SceneElementTag(
        val kind: Int,
        val id: Int,
        val location: SceneTile,
        val isInteractable: Boolean
) {

    internal constructor(
            tag: Long,
            plane: Int
    ) : this(
            getKind(tag),
            getId(tag),
            getLocation(tag, plane),
            isInteractable(tag)
    )

    internal val packed: Long get() {
        var uid = id.toLong() shl 17 +
                kind shl 14 +
                location.y shl 7 +
                location.x
        if (!isInteractable) uid = uid or 65536L
        return uid
    }

    override fun toString(): String {
        return "SceneElementTag(kind=$kind, id=$id, location=$location, isInteractable=$isInteractable)"
    }

    companion object {

        fun getKind(tag: Long): Int = (tag shr 14 and 0x3).toInt()

        fun getId(tag: Long): Int = (tag ushr 17).toInt()

        fun getX(tag: Long): Int = (tag and 0x7F).toInt()

        fun getY(tag: Long): Int = (tag shr 7 and 0x7F).toInt()

        fun getLocation(tag: Long, plane: Int): SceneTile = SceneTile(getX(tag), getY(tag), plane)

        fun isInteractable(tag: Long): Boolean = tag and 65536L == 0L
    }
}
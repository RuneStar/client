package org.runestar.client.game.api

import org.runestar.client.game.raw.Client

data class EntityTag(
        val kind: EntityKind,
        val id: Int,
        val location: SceneTile,
        val isInteractable: Boolean
) {

    internal constructor(
            packed: Long,
            plane: Int = Client.accessor.plane
    ) : this(
            EntityKind.of((packed shr 14 and 0x3).toInt()),
            (packed ushr 17).toInt(),
            SceneTile((packed and 0x7F).toInt(), (packed shr 7 and 0x7F).toInt(), plane),
            packed and 65536L == 0L
    )

    internal val packed: Long get() {
        var uid = id.toLong() shl 17 +
                kind.id shl 14 +
                location.y shl 7 +
                location.x
        if (!isInteractable) uid = uid or 65536L
        return uid
    }

    override fun toString(): String {
        return "EntityTag(kind=$kind, id=$id, location=$location, isInteractable=$isInteractable)"
    }
}
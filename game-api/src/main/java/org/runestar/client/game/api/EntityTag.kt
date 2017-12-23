package org.runestar.client.game.api

import org.runestar.client.game.raw.Client

data class EntityTag(
        val kind: EntityKind,
        val id: Int,
        val location: SceneTile,
        val isInteractable: Boolean
) {
    internal constructor(packed: Int, plane: Int = Client.accessor.plane) : this(
            EntityKind.LOOKUP.getValue(packed shr 29 and 0x3),
            packed shr 14 and 0x7FFF,
            SceneTile(packed and 0x7F, packed shr 7 and 0x7F, plane),
            packed > 0
    )

    internal val packed: Int get() {
        var uid = kind.id shl 29 + id shl 14 + location.y shl 7 + location.x
        if (!isInteractable) uid -= Int.MAX_VALUE
        return uid
    }

    override fun toString(): String {
        return "EntityTag(kind=$kind, id=$id, location=$location, isInteractable=$isInteractable)"
    }
}
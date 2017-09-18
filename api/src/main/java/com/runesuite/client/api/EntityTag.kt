package com.runesuite.client.api

import com.runesuite.client.raw.Client

data class EntityTag(
        val kind: EntityKind,
        val id: Int,
        val location: SceneTile,
        val interactable: Boolean
) {
    internal constructor(packed: Int) : this(
            EntityKind.LOOKUP[packed shr 29 and 0x3]!!,
            packed shr 14 and 0x7FFF,
            SceneTile(packed and 0x7F, packed shr 7 and 0x7F, Client.accessor.plane),
            packed > 0
    )

    internal val packed: Int get() {
        var uid = kind.id shl 29 + id shl 14 + location.y shl 7 + location.x
        if (!interactable) uid -= Int.MAX_VALUE
        return uid
    }

    override fun toString(): String {
        return "EntityTag(kind=$kind, id=$id, location=$location, interactable=$interactable)"
    }
}
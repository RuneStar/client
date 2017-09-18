package com.runesuite.client.core.api

import com.runesuite.client.core.raw.Client

data class EntityTag(
        val kind: com.runesuite.client.core.api.EntityKind,
        val id: Int,
        val location: SceneTile,
        val interactable: Boolean
) {
    internal constructor(packed: Int) : this(
            com.runesuite.client.core.api.EntityKind.Companion.LOOKUP[packed shr 29 and 0x3]!!,
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
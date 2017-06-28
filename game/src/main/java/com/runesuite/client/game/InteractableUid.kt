package com.runesuite.client.game

import com.runesuite.client.base.Client

data class InteractableUid(
        val kind: InteractableKind,
        val id: Int,
        val location: SceneTile,
        val isInteractable: Boolean
) {
    internal constructor(packed: Int) : this(
            InteractableKind.LOOKUP[packed shr 29 and 0x3]!!,
            packed shr 14 and 0x7FFF,
            SceneTile(packed and 0x7F, packed shr 7 and 0x7F, Client.accessor.plane),
            packed > 0
    )

    internal val packed: Int get() {
        var uid = kind.id shl 29 + id shl 14 + location.y shl 7 + location.x
        if (!isInteractable) uid -= Int.MAX_VALUE
        return uid
    }

    override fun toString(): String {
        return "InteractableUid(kind=$kind, id=$id, location=$location, isInteractable=$isInteractable)"
    }
}
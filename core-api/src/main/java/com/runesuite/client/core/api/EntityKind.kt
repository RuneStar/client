package com.runesuite.client.core.api

enum class EntityKind(val id: Int) {

    PLAYER(0),
    NPC(1),
    OBJECT(2),
    GROUND_ITEM(3);

    companion object {
        val LOOKUP = values().associateBy { it.id }
    }
}
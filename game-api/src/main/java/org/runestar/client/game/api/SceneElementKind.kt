package org.runestar.client.game.api

enum class SceneElementKind(val id: Int) {

    PLAYER(0),
    NPC(1),
    OBJECT(2),
    GROUND_ITEM(3);

    companion object {

        @JvmField val VALUES = values().asList()

        fun of(id: Int): SceneElementKind {
            return VALUES[id]
        }
    }
}
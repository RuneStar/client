package org.runestar.client.game.api

enum class ClanRank(val id: Int) {

    FRIEND(0),
    RECRUIT(1),
    CORPORAL(2),
    SERGEANT(3),
    LIEUTENANT(4),
    CAPTAIN(5),
    GENERAL(6),
    OWNER(7);

    companion object {

        @JvmField val VALUES = values().asList()

        fun of(id: Int): ClanRank? {
            if (id < 0) return null
            return VALUES[id]
        }
    }
}
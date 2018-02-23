package org.runestar.client.game.api

enum class ClanRank(val id: Int) {

    NONE(-1),
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

        @JvmStatic
        fun of(id: Int): ClanRank {
            return VALUES[id + 1]
        }
    }
}
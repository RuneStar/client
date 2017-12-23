package org.runestar.client.game.api

enum class ClanRank(val id: Byte) {

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

        @JvmField
        val LOOKUP = values().associateBy { it.id }
    }
}
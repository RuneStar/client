package org.runestar.client.game.api

enum class MessageType(val id: Int) {

    SYSTEM(0),
    PUBLIC_CHAT(2),
    CLAN_CHAT(9),
    SYSTEM_CLAN_CHAT(11),
    ITEM_EXAMINE(27),
    NPC_EXAMINE(28),
    OBJECT_EXAMINE(29),
    SYSTEM_FRIENDS(30),
    SYSTEM_IGNORE(31),
    AUTO_CHAT(90),
    // todo
    SYSTEM_FILTERED(105);

    companion object {
        @JvmField val LOOKUP = values().associateBy { it.id }
    }
}
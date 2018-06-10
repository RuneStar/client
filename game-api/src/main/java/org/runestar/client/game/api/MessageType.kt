package org.runestar.client.game.api

enum class MessageType(val id: Int) {

    SYSTEM(0),
    PUBLIC_MOD(1),
    PUBLIC(2),
    PRIVATE_RECEIVED(3),
    PRIVATE_INFO(5),
    PRIVATE_SENT(6),
    PRIVATE_RECEIVED_MOD(7),
    CLAN_CHAT(9),
    SYSTEM_CLAN_CHAT(11),
    ITEM_EXAMINE(27),
    NPC_EXAMINE(28),
    OBJECT_EXAMINE(29),
    SYSTEM_FRIENDS(30),
    SYSTEM_IGNORE(31),
    AUTO_CHAT(90),
    TRADE_RECEIVED(101), // XX wishes to trade with you
    TRADE_INFO(102), // Other player declined trade; accepted trade
    // todo
    SYSTEM_FILTERED(105);

    companion object {
        @JvmField val LOOKUP = values().associateBy { it.id }
    }
}
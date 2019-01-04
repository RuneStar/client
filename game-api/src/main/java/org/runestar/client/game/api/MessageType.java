package org.runestar.client.game.api;

public final class MessageType {
    
    private MessageType() {}

    public static final int SYSTEM = 0;
    public static final int PUBLIC_MOD = 1;
    public static final int PUBLIC = 2;
    public static final int PRIVATE_RECEIVED = 3;
    public static final int PRIVATE_INFO = 5;
    public static final int PRIVATE_SENT = 6;
    public static final int PRIVATE_RECEIVED_MOD = 7;
    public static final int CLAN_CHAT = 9;
    public static final int SYSTEM_CLAN_CHAT = 11;
    public static final int ITEM_EXAMINE = 27;
    public static final int NPC_EXAMINE = 28;
    public static final int OBJECT_EXAMINE = 29;
    public static final int SYSTEM_FRIENDS = 30;
    public static final int SYSTEM_IGNORE = 31;
    public static final int AUTO_CHAT = 90;
    public static final int AUTO_CHAT_MOD = 91; // todo
    public static final int TRADE_RECEIVED = 101; // XX wishes to trade with you
    public static final int TRADE_INFO = 102; // Other player declined trade / Accepted trade
    public static final int SYSTEM_FILTERED = 105;

    // todo
}

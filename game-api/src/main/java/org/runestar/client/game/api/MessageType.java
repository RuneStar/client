package org.runestar.client.game.api;

public final class MessageType {
    
    private MessageType() {}

    public static int SYSTEM = 0;
    public static int PUBLIC_MOD = 1;
    public static int PUBLIC = 2;
    public static int PRIVATE_RECEIVED = 3;
    public static int PRIVATE_INFO = 5;
    public static int PRIVATE_SENT = 6;
    public static int PRIVATE_RECEIVED_MOD = 7;
    public static int CLAN_CHAT = 9;
    public static int SYSTEM_CLAN_CHAT = 11;
    public static int ITEM_EXAMINE = 27;
    public static int NPC_EXAMINE = 28;
    public static int OBJECT_EXAMINE = 29;
    public static int SYSTEM_FRIENDS = 30;
    public static int SYSTEM_IGNORE = 31;
    public static int AUTO_CHAT = 90;
    public static int AUTO_CHAT_MOD = 91; // todo
    public static int TRADE_RECEIVED = 101; // XX wishes to trade with you
    public static int TRADE_INFO = 102; // Other player declined trade / Accepted trade
    public static int SYSTEM_FILTERED = 105;

    // todo
}

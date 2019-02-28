package org.runestar.client.game.api;

public final class ChatType {

    private ChatType() {}

    public static final int
            GAMEMESSAGE = 0,
            MODCHAT = 1,
            PUBLICCHAT = 2,
            PRIVATECHAT = 3,
            ENGINE = 4,
            LOGINLOGOUTNOTIFICATION = 5,
            PRIVATECHATOUT = 6,
            MODPRIVATECHAT = 7,
            FRIENDSCHAT = 9,
            FRIENDSCHATNOTIFICATION = 11,
            BROADCAST = 14,
            SNAPSHOTFEEDBACK = 26,
            OBJ_EXAMINE = 27,
            NPC_EXAMINE = 28,
            LOC_EXAMINE = 29,
            FRIENDNOTIFICATION = 30,
            IGNORENOTIFICATION = 31,
            AUTOTYPER = 90,
            MODAUTOTYPER = 91,
            CONSOLE = 99,
            TRADEREQ = 101,
            TRADE = 102,
            CHALREQ_TRADE = 103,
            CHALREQ_FRIENDSCHAT = 104,
            SPAM = 105,
            PLAYERRELATED = 106,
            _10SECTIMEOUT = 107;
}

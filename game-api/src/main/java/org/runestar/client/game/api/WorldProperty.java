package org.runestar.client.game.api;

public final class WorldProperty {
    
    private WorldProperty() {}

    public static final int MEMBERS =               1;
    public static final int PVP =                   1 << 2;
    public static final int BOUNTY_HUNTER =         1 << 5;
    public static final int SKILL_TOTAL =           1 << 7;
    public static final int HIGH_RISK =             1 << 10;
    public static final int LAST_MAN_STANDING =     1 << 14;
    public static final int TOURNAMENT =            1 << 25;
    public static final int DEADMAN_TOURNAMENT =    1 << 26;
    public static final int DEADMAN =               1 << 29;
    public static final int SEASONAL =              1 << 30;
}

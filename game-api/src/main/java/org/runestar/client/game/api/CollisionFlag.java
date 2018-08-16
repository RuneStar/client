package org.runestar.client.game.api;

public final class CollisionFlag {
    
    private CollisionFlag() {}

    public static final int WALK_NORTHWEST =  1;
    public static final int WALK_NORTH =      1 << 1;
    public static final int WALK_NORTHEAST =  1 << 2;
    public static final int WALK_EAST =       1 << 3;
    public static final int WALK_SOUTHEAST =  1 << 4;
    public static final int WALK_SOUTH =      1 << 5;
    public static final int WALK_SOUTHWEST =  1 << 6;
    public static final int WALK_WEST =       1 << 7;
    public static final int WALK_OBJECT =     1 << 8;
    public static final int PROJECTILE_NORTHWEST =    1 << 9;
    public static final int PROJECTILE_NORTH =        1 << 10;
    public static final int PROJECTILE_NORTHEAST =    1 << 11;
    public static final int PROJECTILE_EAST =         1 << 12;
    public static final int PROJECTILE_SOUTHEAST =    1 << 13;
    public static final int PROJECTILE_SOUTH =        1 << 14;
    public static final int PROJECTILE_SOUTHWEST =    1 << 15;
    public static final int PROJECTILE_WEST =         1 << 16;
    public static final int PROJECTILE_OBJECT =       1 << 17;

    public static final int WALK_GROUND =             1 << 21;
}

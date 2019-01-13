package org.runestar.client.game.api;

public final class GameState {

    private GameState() {}

    public static final int NONE = 0;
    public static final int STARTING = 5;
    public static final int TITLE = 10;
    public static final int AUTHENTICATOR = 11;
    public static final int LOGGING_IN = 20;
    public static final int LOADING = 25;
    public static final int LOGGED_IN = 30;
    public static final int CONNECTION_LOST = 40;
    public static final int CHANGING_WORLD = 45;
}

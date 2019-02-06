package org.runestar.client.game.api;

public final class GrandExchangeOfferStatus {

    private GrandExchangeOfferStatus() {}

    public static final int STARTING = 1;
    public static final int IDLE = 2;
    public static final int UPDATING = 3;
    public static final int ABORTING = 4;
    public static final int DONE = 5;
}

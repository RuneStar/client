package org.runestar.client.game.api

enum class GrandExchangeOfferStatus(val id: Int) {

    STARTING(1),
    IDLE(2),
    UPDATING(3),
    ABORTING(4),
    DONE(5);

    companion object {

        @JvmField val VALUES = values().asList()

        fun of (id: Int): GrandExchangeOfferStatus {
            return VALUES[id - 1]
        }
    }
}
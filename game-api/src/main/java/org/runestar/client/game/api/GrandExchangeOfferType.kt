package org.runestar.client.game.api

enum class GrandExchangeOfferType(val id: Int) {

    BUY(0),
    SELL(1);

    companion object {
        @JvmField val LOOKUP = values().associateBy { it.id }
    }
}
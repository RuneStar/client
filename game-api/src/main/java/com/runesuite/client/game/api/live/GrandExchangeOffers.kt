package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.GrandExchangeOffer
import com.runesuite.client.game.raw.Client.accessor

object GrandExchangeOffers {

    val SIZE = accessor.grandExchangeOffers.size

    val all: List<com.runesuite.client.game.api.GrandExchangeOffer> get() {
        return accessor.grandExchangeOffers.mapNotNull { it?.takeIf { it.id != 0 }?.let { com.runesuite.client.game.api.GrandExchangeOffer(it) } }
    }

    fun get(): List<com.runesuite.client.game.api.GrandExchangeOffer?> {
        return accessor.grandExchangeOffers.map { it?.takeIf { it.id != 0 }?.let { com.runesuite.client.game.api.GrandExchangeOffer(it) } }
    }

    operator fun get(index: Int): com.runesuite.client.game.api.GrandExchangeOffer? {
        return accessor.grandExchangeOffers[index]?.takeIf { it.id != 0 }?.let { com.runesuite.client.game.api.GrandExchangeOffer(it) }
    }
}
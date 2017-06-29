package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.GrandExchangeOffer

object GrandExchangeOffers {

    val SIZE = accessor.grandExchangeOffers.size

    val all: Sequence<GrandExchangeOffer> get() {
        return accessor.grandExchangeOffers.copyOf().asSequence().filterNotNull().filter { it.id != 0 }.map { GrandExchangeOffer(it) }
    }

    fun get(): List<GrandExchangeOffer?> {
        return accessor.grandExchangeOffers.copyOf().map { it?.takeIf { it.id != 0 }?.let { GrandExchangeOffer(it) } }
    }

    operator fun get(index: Int): GrandExchangeOffer? {
        return accessor.grandExchangeOffers[index]?.takeIf { it.id != 0 }?.let { GrandExchangeOffer(it) }
    }
}
package com.runesuite.client.core.api.live

import com.runesuite.client.core.raw.Client.accessor

object GrandExchangeOffers {

    val SIZE = accessor.grandExchangeOffers.size

    val all: List<com.runesuite.client.core.api.GrandExchangeOffer> get() {
        return accessor.grandExchangeOffers.mapNotNull { it?.takeIf { it.id != 0 }?.let { com.runesuite.client.core.api.GrandExchangeOffer(it) } }
    }

    fun get(): List<com.runesuite.client.core.api.GrandExchangeOffer?> {
        return accessor.grandExchangeOffers.map { it?.takeIf { it.id != 0 }?.let { com.runesuite.client.core.api.GrandExchangeOffer(it) } }
    }

    operator fun get(index: Int): com.runesuite.client.core.api.GrandExchangeOffer? {
        return accessor.grandExchangeOffers[index]?.takeIf { it.id != 0 }?.let { com.runesuite.client.core.api.GrandExchangeOffer(it) }
    }
}
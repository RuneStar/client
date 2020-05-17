package org.runestar.client.api.game.live

import org.runestar.client.api.game.GrandExchangeOffer
import org.runestar.client.raw.CLIENT

object GrandExchangeOffers : AbstractList<GrandExchangeOffer?>(), RandomAccess {

    override val size get() = 8

    override fun get(index: Int): GrandExchangeOffer? {
        return CLIENT.grandExchangeOffers[index]?.let { GrandExchangeOffer.of(it) }
    }
}
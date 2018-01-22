package org.runestar.client.game.api.live

import org.runestar.client.game.api.GrandExchangeOffer
import org.runestar.client.game.raw.Client.accessor

object GrandExchangeOffers : AbstractList<GrandExchangeOffer?>(), RandomAccess {

    override val size get() = 8

    override fun get(index: Int) = accessor.grandExchangeOffers[index]?.let { GrandExchangeOffer.of(it) }
}
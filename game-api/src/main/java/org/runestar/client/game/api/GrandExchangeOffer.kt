package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XGrandExchangeOffer

class GrandExchangeOffer(val accessor: XGrandExchangeOffer) {

    init {
        require(accessor.id != 0)
    }

    val id get() = accessor.id

    val totalQuantity get() = accessor.totalQuantity

    val currentPrice get() = accessor.currentPrice

    val unitPrice get() = accessor.unitPrice

    val totalPrice get() = unitPrice * totalQuantity

    val currentQuantity get() = accessor.currentQuantity

    val isFilled get() = totalQuantity == currentQuantity

    val type get() = GrandExchangeOfferType.of(accessor.type())

    val status get() = GrandExchangeOfferStatus.of(accessor.status())

    val isAborted get() = when(status) {
        GrandExchangeOfferStatus.ABORTING -> true
        GrandExchangeOfferStatus.DONE -> totalQuantity != currentQuantity
        else -> false
    }

    override fun toString(): String {
        return "GrandExchangeOffer(type=$type, id=$id, unitPrice=$unitPrice, currentQuantity=$currentQuantity, totalQuantity=$totalQuantity, status=$status)"
    }

    companion object {

        @JvmStatic
        fun of(accessor: XGrandExchangeOffer): GrandExchangeOffer? {
            if (accessor.id == 0) return null
            return GrandExchangeOffer(accessor)
        }
    }
}
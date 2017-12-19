package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XGrandExchangeOffer

class GrandExchangeOffer(override val accessor: XGrandExchangeOffer) : Wrapper() {

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

    val type get() = GrandExchangeOfferType.LOOKUP.getValue(accessor.type())

    val status get() = GrandExchangeOfferStatus.LOOKUP.getValue(accessor.status())

    val isAborted get() = when(status) {
        GrandExchangeOfferStatus.ABORTING -> true
        GrandExchangeOfferStatus.DONE -> totalQuantity != currentQuantity
        else -> false
    }

    override fun toString(): String {
        return "GrandExchangeOffer(type=$type, id=$id, unitPrice=$unitPrice, currentQuantity=$currentQuantity, totalQuantity=$totalQuantity, status=$status)"
    }
}
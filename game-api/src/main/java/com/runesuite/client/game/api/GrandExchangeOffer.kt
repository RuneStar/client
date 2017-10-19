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

    val type get() = checkNotNull(com.runesuite.client.game.api.GrandExchangeOffer.Type.Companion.LOOKUP[accessor.type()]) { accessor.type() }

    val status get() = checkNotNull(com.runesuite.client.game.api.GrandExchangeOffer.Status.Companion.LOOKUP[accessor.status()]) { accessor.status() }

    val isAborted get() = when(status) {
        com.runesuite.client.game.api.GrandExchangeOffer.Status.ABORTING -> true
        com.runesuite.client.game.api.GrandExchangeOffer.Status.DONE -> totalQuantity != currentQuantity
        else -> false
    }

    override fun toString(): String {
        return "GrandExchangeOffer(type=$type, id=$id, unitPrice=$unitPrice, currentQuantity=$currentQuantity, totalQuantity=$totalQuantity, status=$status)"
    }

    enum class Type(val id: Int) {
        BUY(0),
        SELL(1);

        companion object {
            @JvmField
            val LOOKUP = values().associateBy { it.id }
        }
    }

    enum class Status(val id: Int) {
        STARTING(1),
        IDLE(2),
        UPDATING(3),
        ABORTING(4),
        DONE(5);

        companion object {
            @JvmField
            val LOOKUP = values().associateBy { it.id }
        }
    }
}
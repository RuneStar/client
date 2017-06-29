package com.runesuite.client.game

import com.runesuite.client.base.Wrapper
import com.runesuite.client.base.access.XGrandExchangeOffer

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

    val type get() = checkNotNull(Type.LOOKUP[accessor.type()]) { accessor.type() }

    val status get() = checkNotNull(Status.LOOKUP[accessor.status()]) { accessor.status() }

    val isAborted get() = when(status) {
        Status.ABORTING -> true
        Status.DONE -> totalQuantity != currentQuantity
        else -> false
    }

    enum class Type(val id: Int) {
        BUY(0),
        SELL(8);

        companion object {
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
            val LOOKUP = values().associateBy { it.id }
        }
    }
}
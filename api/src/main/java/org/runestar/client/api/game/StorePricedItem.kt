package org.runestar.client.api.game

interface StorePricedItem {

    val storePrice: Int

    val highAlchemyValue get() = highAlchemyValue(storePrice)

    val lowAlchemyValue get() = lowAlchemyValue(storePrice)

    companion object {

        fun highAlchemyValue(storePrice: Int) = 3 * storePrice / 5

        fun lowAlchemyValue(storePrice: Int) = 2 * storePrice / 5
    }
}
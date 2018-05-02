package org.runestar.client.game.api

data class Item(
        val id: Int,
        val quantity: Int
) {

    init {
        require(id >= 0 && quantity > 0)
    }

    companion object {

        fun of(id: Int, quantity: Int): Item? {
            if (id < 0 || quantity <= 0) return null
            return Item(id, quantity)
        }
    }
}
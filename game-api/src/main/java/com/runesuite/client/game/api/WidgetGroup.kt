package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Client

data class WidgetGroup(val id: Int) {

    val size get() = Client.accessor.widgets[id]?.size

    val flat: List<com.runesuite.client.game.api.Widget> get() {
        return all.flatMap { it.flat }
    }

    val all: List<com.runesuite.client.game.api.Widget> get() {
        return Client.accessor.widgets[id]?.map { com.runesuite.client.game.api.Widget(it) } ?: emptyList()
    }

    operator fun get(id: Int): com.runesuite.client.game.api.Widget? {
        return Client.accessor.widgets[this.id]?.get(id)?.let { com.runesuite.client.game.api.Widget(it) }
    }
}
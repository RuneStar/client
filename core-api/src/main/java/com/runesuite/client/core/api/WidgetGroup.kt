package com.runesuite.client.core.api

import com.runesuite.client.core.raw.Client

data class WidgetGroup(val id: Int) {

    val size get() = Client.accessor.widgets[id]?.size

    val flat: List<com.runesuite.client.core.api.Widget> get() {
        return all.flatMap { it.flat }
    }

    val all: List<com.runesuite.client.core.api.Widget> get() {
        return Client.accessor.widgets[id]?.map { com.runesuite.client.core.api.Widget(it) } ?: emptyList()
    }

    operator fun get(id: Int): com.runesuite.client.core.api.Widget? {
        return Client.accessor.widgets[this.id]?.get(id)?.let { com.runesuite.client.core.api.Widget(it) }
    }
}
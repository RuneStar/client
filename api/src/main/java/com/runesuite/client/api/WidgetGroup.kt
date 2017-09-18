package com.runesuite.client.api

import com.runesuite.client.raw.Client

data class WidgetGroup(val id: Int) {

    val size get() = Client.accessor.widgets[id]?.size

    val flat: List<Widget> get() {
        return all.flatMap { it.flat }
    }

    val all: List<Widget> get() {
        return Client.accessor.widgets[id]?.map { Widget(it) } ?: emptyList()
    }

    operator fun get(id: Int): Widget? {
        return Client.accessor.widgets[this.id]?.get(id)?.let { Widget(it) }
    }
}
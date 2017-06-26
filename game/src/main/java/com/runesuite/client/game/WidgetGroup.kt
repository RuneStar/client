package com.runesuite.client.game

import com.runesuite.client.base.Client

data class WidgetGroup(val id: Int) {

    val size get() = Client.accessor.widgets[id]?.size

    val all: Sequence<Widget> get() {
        return get().asSequence().flatMap { it.all }
    }

    fun get(): List<Widget> {
        return Client.accessor.widgets[id]?.copyOf()?.map { Widget(it) } ?: emptyList()
    }

    operator fun get(id: Int): Widget? {
        return Client.accessor.widgets[this.id]?.get(id)?.let { Widget(it) }
    }
}
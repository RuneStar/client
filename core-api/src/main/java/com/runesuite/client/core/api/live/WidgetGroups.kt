package com.runesuite.client.core.api.live

import com.runesuite.client.core.raw.Client

object WidgetGroups {

    val SIZE = Client.accessor.widgets.size

    val all: List<com.runesuite.client.core.api.WidgetGroup>
        get() = Client.accessor.widgets.withIndex().filter { it.value != null }.map { com.runesuite.client.core.api.WidgetGroup(it.index) }

    fun get(): List<com.runesuite.client.core.api.WidgetGroup?> = Client.accessor.widgets.withIndex().map { iv -> com.runesuite.client.core.api.WidgetGroup(iv.index).takeIf { iv.value != null } }

    operator fun get(id: Int): com.runesuite.client.core.api.WidgetGroup? {
        return com.runesuite.client.core.api.WidgetGroup(id).takeIf { Client.accessor.widgets[id] != null }
    }
}
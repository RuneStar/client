package com.runesuite.client.api.live

import com.runesuite.client.api.WidgetGroup
import com.runesuite.client.raw.Client

object WidgetGroups {

    val SIZE = Client.accessor.widgets.size

    val all: List<WidgetGroup>
        get() = Client.accessor.widgets.withIndex().filter { it.value != null }.map { WidgetGroup(it.index) }

    fun get(): List<WidgetGroup?> = Client.accessor.widgets.withIndex().map { iv -> WidgetGroup(iv.index).takeIf { iv.value != null } }

    operator fun get(id: Int): WidgetGroup? {
        return WidgetGroup(id).takeIf { Client.accessor.widgets[id] != null }
    }
}
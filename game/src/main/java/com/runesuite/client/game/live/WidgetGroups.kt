package com.runesuite.client.game.live

import com.runesuite.client.base.Client
import com.runesuite.client.game.WidgetGroup

object WidgetGroups {

    val SIZE = Client.accessor.widgets.size

    val all: Sequence<WidgetGroup>
        get() = Client.accessor.widgets.copyOf().asSequence().withIndex().filter { it.value != null }.map { WidgetGroup(it.index) }

    fun get(): List<WidgetGroup?> = Client.accessor.widgets.copyOf().withIndex().map { iv -> WidgetGroup(iv.index).takeIf { iv.value != null } }

    operator fun get(id: Int): WidgetGroup? {
        return WidgetGroup(id).takeIf { Client.accessor.widgets[id] != null }
    }
}
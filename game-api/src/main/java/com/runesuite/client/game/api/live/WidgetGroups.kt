package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.WidgetGroup
import com.runesuite.client.game.raw.Client

object WidgetGroups {

    val CAPACITY = Client.accessor.widgets.size

    val all: List<WidgetGroup> get() =
        Client.accessor.widgets.withIndex().filter { it.value != null }.map { WidgetGroup(it.index) }

    fun get(): List<WidgetGroup?> =
            Client.accessor.widgets.withIndex().map { iv -> WidgetGroup(iv.index).takeIf { iv.value != null } }

    operator fun get(id: Int): WidgetGroup? = Client.accessor.widgets.getOrNull(id)?.let { WidgetGroup(id) }

    val root: WidgetGroup? get() = get(Client.accessor.rootWidgetGroup)
}
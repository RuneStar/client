package org.runestar.client.game.api.live

import org.runestar.client.game.api.WidgetGroup
import org.runestar.client.game.raw.Client

object WidgetGroups {

    val CAPACITY = Client.accessor.widgets.size

    val all: List<WidgetGroup> get() =
        Client.accessor.widgets.withIndex().mapNotNull { iv -> iv.value?.let { WidgetGroup(iv.index, it) } }

    fun get(): List<WidgetGroup?> =
            Client.accessor.widgets.withIndex().map { iv -> iv.value?.let { WidgetGroup(iv.index, it) } }

    operator fun get(id: Int): WidgetGroup? = Client.accessor.widgets.getOrNull(id)?.let { WidgetGroup(id, it) }

    val root: WidgetGroup? get() = get(Client.accessor.rootWidgetGroup)
}
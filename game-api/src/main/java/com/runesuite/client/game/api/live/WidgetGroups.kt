package com.runesuite.client.game.api.live

import com.runesuite.client.game.raw.Client

object WidgetGroups {

    val SIZE = Client.accessor.widgets.size

    val all: List<com.runesuite.client.game.api.WidgetGroup>
        get() = Client.accessor.widgets.withIndex().filter { it.value != null }.map { com.runesuite.client.game.api.WidgetGroup(it.index) }

    fun get(): List<com.runesuite.client.game.api.WidgetGroup?> = Client.accessor.widgets.withIndex().map { iv -> com.runesuite.client.game.api.WidgetGroup(iv.index).takeIf { iv.value != null } }

    operator fun get(id: Int): com.runesuite.client.game.api.WidgetGroup? {
        return com.runesuite.client.game.api.WidgetGroup(id).takeIf { Client.accessor.widgets[id] != null }
    }
}
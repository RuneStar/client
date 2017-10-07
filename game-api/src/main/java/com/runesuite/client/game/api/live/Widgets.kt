package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Widget

object Widgets {

    val flat: List<com.runesuite.client.game.api.Widget> get() {
        return WidgetGroups.all.flatMap { it.flat }
    }

    fun get(): List<List<com.runesuite.client.game.api.Widget>?> {
        return WidgetGroups.get().map { it?.all }
    }
}
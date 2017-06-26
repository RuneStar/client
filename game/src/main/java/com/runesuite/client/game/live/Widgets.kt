package com.runesuite.client.game.live

import com.runesuite.client.game.Widget

object Widgets {

    val all: Sequence<Widget> get() {
        return WidgetGroups.all.flatMap { it.all }
    }

    fun get(): List<List<Widget>?> {
        return WidgetGroups.get().map { it?.get() }
    }
}
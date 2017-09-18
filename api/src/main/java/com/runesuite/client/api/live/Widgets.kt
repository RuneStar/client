package com.runesuite.client.api.live

import com.runesuite.client.api.Widget

object Widgets {

    val flat: List<Widget> get() {
        return WidgetGroups.all.flatMap { it.flat }
    }

    fun get(): List<List<Widget>?> {
        return WidgetGroups.get().map { it?.all }
    }
}
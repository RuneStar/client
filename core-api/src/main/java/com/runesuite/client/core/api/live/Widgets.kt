package com.runesuite.client.core.api.live

object Widgets {

    val flat: List<com.runesuite.client.core.api.Widget> get() {
        return WidgetGroups.all.flatMap { it.flat }
    }

    fun get(): List<List<com.runesuite.client.core.api.Widget>?> {
        return WidgetGroups.get().map { it?.all }
    }
}
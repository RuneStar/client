package org.runestar.client.plugins.dev.widgetexplorer

import org.runestar.client.game.api.Widget

data class WidgetWrapper(val widget: Widget) {

    override fun toString(): String {
        return widget.idString()
    }
}
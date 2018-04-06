package org.runestar.client.plugins.std.widgetexplorer

import org.runestar.client.game.api.Widget

data class WidgetWrapper(val widget: Widget) {

    override fun toString(): String {
        return widget.idString()
    }
}
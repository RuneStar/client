package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XNpcDefinition

inline class NpcDefinition(val accessor: XNpcDefinition) {

    val id get() = accessor.id

    val actions: Array<String?> get() = accessor.actions

    val name: String? get() = accessor.name.takeUnless { it == "null" }

    val headIconPrayer get() = HeadIconPrayer.of(accessor.headIconPrayer)

    var drawMapDot: Boolean
        get() = accessor.drawMapDot
        set(value) { accessor.drawMapDot = value }

    fun recolor(from: Short, to: Short) {
        if (accessor.recolorFrom == null) {
            accessor.recolorFrom = shortArrayOf(from)
            accessor.recolorTo = shortArrayOf(to)
        } else {
            val i = accessor.recolorFrom.indexOf(from)
            if (i == -1) {
                accessor.recolorFrom = accessor.recolorFrom.plus(from)
                accessor.recolorTo = accessor.recolorTo.plus(to)
            } else {
                accessor.recolorTo[i] = to
            }
        }
    }
}
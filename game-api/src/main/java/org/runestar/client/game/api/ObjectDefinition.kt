package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XObjectDefinition

inline class ObjectDefinition(val accessor: XObjectDefinition) {

    val id get() = accessor.id

    fun recolor(from: HslColor, to: HslColor) {
        if (accessor.recolorFrom == null) {
            accessor.recolorFrom = shortArrayOf(from.packed)
            accessor.recolorTo = shortArrayOf(to.packed)
        } else {
            val i = accessor.recolorFrom.indexOf(from.packed)
            if (i == -1) {
                accessor.recolorFrom = accessor.recolorFrom.plus(from.packed)
                accessor.recolorTo = accessor.recolorTo.plus(to.packed)
            } else {
                accessor.recolorTo[i] = to.packed
            }
        }
    }
}
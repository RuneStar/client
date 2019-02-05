package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XObjectDefinition

inline class ObjectDefinition(val accessor: XObjectDefinition) {

    val id get() = accessor.id

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
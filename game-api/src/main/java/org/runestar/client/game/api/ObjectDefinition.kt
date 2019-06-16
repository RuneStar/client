package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XLocType

inline class ObjectDefinition(val accessor: XLocType) {

    val id get() = accessor.id

    fun recolor(from: HslColor, to: HslColor) {
        if (accessor.recol_s == null) {
            accessor.recol_s = shortArrayOf(from.packed)
            accessor.recol_d = shortArrayOf(to.packed)
        } else {
            val i = accessor.recol_s.indexOf(from.packed)
            if (i == -1) {
                accessor.recol_s = accessor.recol_s.plus(from.packed)
                accessor.recol_d = accessor.recol_d.plus(to.packed)
            } else {
                accessor.recol_d[i] = to.packed
            }
        }
    }
}
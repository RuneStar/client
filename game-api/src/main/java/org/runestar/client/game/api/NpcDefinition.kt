package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XNPCType

inline class NpcDefinition(val accessor: XNPCType) {

    val id get() = accessor.id

    val actions: Array<String?> get() = accessor.op

    val name: String? get() = accessor.name.takeUnless { it == "null" }

    val headIconPrayer get() = HeadIconPrayer.of(accessor.headIconPrayer)

    var drawMapDot: Boolean
        get() = accessor.drawMapDot
        set(value) { accessor.drawMapDot = value }

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
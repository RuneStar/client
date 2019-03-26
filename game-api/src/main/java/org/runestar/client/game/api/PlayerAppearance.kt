package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XPlayerAppearance
import java.util.EnumMap

inline class PlayerAppearance(val accessor: XPlayerAppearance) {

    val items: Map<EquipmentSlot, Int> get() {
        return EnumMap<EquipmentSlot, Int>(EquipmentSlot::class.java).apply {
            val equip = accessor.equipment
            for (v in EquipmentSlot.VALUES) {
                val id = equip[v.id]
                if (id >= 512) {
                    check(v.supportsItems && v.isVisible)
                    put(v, id - 512)
                }
            }
        }
    }

    val kit: Map<EquipmentSlot, Int> get() {
        return EnumMap<EquipmentSlot, Int>(EquipmentSlot::class.java).apply {
            val equip = accessor.equipment
            for (v in EquipmentSlot.VALUES) {
                val id = equip[v.id]
                if (id in 256..511) {
                    check(v.isVisible)
                    put(v, id - 256)
                }
            }
        }
    }

    val bodyColors: IntArray get() = accessor.bodyColors

    val sex get() = if (accessor.isFemale) Sex.FEMALE else Sex.MALE
}
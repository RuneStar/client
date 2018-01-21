package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XPlayerAppearance
import java.util.*

class PlayerAppearance(val accessor: XPlayerAppearance) {

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

    val bodyColors: Map<BodyPart, Int> get() {
        return EnumMap<BodyPart, Int>(BodyPart::class.java).apply {
            val bcs = accessor.bodyColors
            for (v in BodyPart.VALUES) {
                put(v, bcs[v.id])
            }
        }
    }

    val sex get() = if (accessor.isFemale) Sex.FEMALE else Sex.MALE

    enum class BodyPart(val id: Int) {

        HAIR(0),
        BODY(1),
        LEGS(2),
        FEET(3),
        SKIN(4);

        companion object {
            @JvmField val VALUES = values().asList()
        }
    }
}
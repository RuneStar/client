package org.runestar.client.game.api

import org.runestar.client.game.raw.Wrapper
import org.runestar.client.game.raw.access.XPlayerAppearance
import java.util.*

class PlayerAppearance(override val accessor: XPlayerAppearance) : Wrapper() {

    val items: Map<EquipmentSlot.Item, Int> get() {
        return EnumMap<EquipmentSlot.Item, Int>(EquipmentSlot.Item::class.java).apply {
            val equip = accessor.equipment
            for (e in EquipmentSlot.Item.LOOKUP.entries) {
                val id = equip[e.key]
                if (id >= 512) {
                    put(e.value, id - 512)
                }
            }
        }
    }

    val kit: Map<EquipmentSlot.Kit, Int> get() {
        return EnumMap<EquipmentSlot.Kit, Int>(EquipmentSlot.Kit::class.java).apply {
            val equip = accessor.equipment
            for (e in EquipmentSlot.Kit.LOOKUP.entries) {
                val id = equip[e.key]
                if (id in 256..511) {
                    put(e.value, id - 256)
                }
            }
        }
    }

    val bodyColors: Map<BodyPart, Int> get() {
        return EnumMap<BodyPart, Int>(BodyPart::class.java).apply {
            val bcs = accessor.bodyColors
            for (e in BodyPart.LOOKUP.entries) {
                put(e.value, bcs[e.key])
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
            @JvmField val LOOKUP = values().associateBy { it.id }
        }
    }
}
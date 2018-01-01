package org.runestar.client.game.api

import org.runestar.client.game.raw.Wrapper
import org.runestar.client.game.raw.access.XPlayerAppearance

class PlayerAppearance(override val accessor: XPlayerAppearance) : Wrapper() {

    val items: Map<EquipmentSlot.Item, Int>
        get() = accessor.equipment.withIndex()
                .filter { it.value >= 512 }
                .associate { EquipmentSlot.Item.LOOKUP.getValue(it.index) to it.value - 512 }

    val kit: Map<EquipmentSlot.Kit, Int>
        get() = accessor.equipment.withIndex()
                .filter { it.value in 256 until 512 }
                .associate { EquipmentSlot.Kit.LOOKUP.getValue(it.index) to it.value - 256 }

    val bodyColors: Map<PlayerAppearance.BodyPart, Int>
        get() = accessor.bodyColors.withIndex()
                .associate { BodyPart.LOOKUP[it.index]!! to it.value }

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
package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XPlayerAppearance

class PlayerAppearance(override val accessor: XPlayerAppearance) : Wrapper() {

    val equipment: Map<PlayerAppearance.EquipmentSlot, Int>
        get() = accessor.equipment.withIndex()
                .filter { PlayerAppearance.EquipmentSlot.LOOKUP.containsKey(it.index) }
                .associate { PlayerAppearance.EquipmentSlot.LOOKUP[it.index]!! to it.value }

    val bodyColors: Map<PlayerAppearance.BodyPart, Int>
        get() = accessor.bodyColors.withIndex()
                .associate { PlayerAppearance.BodyPart.Companion.LOOKUP[it.index]!! to it.value }

    val sex get() = if (accessor.isFemale) Sex.FEMALE else Sex.MALE

    enum class BodyPart(val id: Int) {

        HAIR(0),
        BODY(1),
        LEGS(2),
        FEET(3),
        SKIN(4);

        companion object {

            @JvmField
            val LOOKUP = values().associateBy { it.id }
        }
    }

    enum class EquipmentSlot(val id: Int) {

        HEAD(0),
        CAPE(1),
        NECK(2),
        WEAPON(3),
        BODY(4),
        SHIELD(5),
        // 6
        LEGS(7),
        // 8
        HANDS(9),
        FEET(10);
        // 11

        companion object {
            
            @JvmField
            val LOOKUP = values().associateBy { it.id }
        }
    }
}
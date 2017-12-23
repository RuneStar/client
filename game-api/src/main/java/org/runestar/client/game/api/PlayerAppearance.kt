package org.runestar.client.game.api

import org.runestar.client.game.raw.Wrapper
import org.runestar.client.game.raw.access.XPlayerAppearance

class PlayerAppearance(override val accessor: XPlayerAppearance) : Wrapper() {

    val equipment: Map<KitPart, Int>
        get() = accessor.equipment.withIndex()
                .filter { it.value >= 512 }
                .associate { KitPart.LOOKUP[it.index]!! to it.value - 512 }

    val kit: Map<KitPart, Int>
        get() = accessor.equipment.withIndex()
                .filter { it.value in 256 until 512 }
                .associate { KitPart.LOOKUP[it.index]!! to it.value - 256 }

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

            @JvmField
            val LOOKUP = values().associateBy { it.id }
        }
    }

    enum class KitPart(val id: Int) {

        HEAD(0),
        CAPE(1),
        NECK(2),
        WEAPON(3),
        BODY(4),
        SHIELD(5),
        ARMS(6),
        LEGS(7),
        HAIR(8),
        HANDS(9),
        FEET(10),
        JAW(11);

        companion object {
            
            @JvmField
            val LOOKUP = values().associateBy { it.id }
        }
    }
}
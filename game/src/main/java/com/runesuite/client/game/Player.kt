package com.runesuite.client.game

import com.runesuite.client.base.Wrapper
import com.runesuite.client.base.access.XPlayer
import com.runesuite.client.base.access.XPlayerAppearance

class Player(override val accessor: XPlayer) : Actor(accessor) {

    val name get() = accessor.name ?: ""

    val actions: List<String> get() = accessor.actions.toList()

    val combatLevel get() = accessor.combatLevel

    val prayerIcon get() = accessor.prayerIcon

    val skullIcon get() = accessor.skullIcon

    val team get() = accessor.team

    val appearance: Appearance? get() = accessor.appearance?.let { Appearance(it) }

    override fun toString(): String {
        return "Player($name)"
    }

    class Appearance(override val accessor: XPlayerAppearance) : Wrapper() {

        val equipment: Map<EquipmentSlot, Int>
            get() = accessor.equipment.withIndex()
                    .filter { EquipmentSlot.LOOKUP.containsKey(it.index) }
                    .associate { EquipmentSlot.LOOKUP[it.index]!! to it.value }

        val bodyColors: Map<BodyPart, Int>
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
                val LOOKUP = values().associateBy { it.id }
            }
        }

        enum class Sex {
            MALE,
            FEMALE
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
                val LOOKUP = values().associateBy { it.id }
            }
        }
    }
}
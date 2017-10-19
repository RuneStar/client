package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XPlayer
import com.runesuite.client.game.raw.access.XPlayerAppearance

class Player(override val accessor: XPlayer) : com.runesuite.client.game.api.Actor(accessor) {

    val name get() = accessor.name ?: ""

    val actions: List<String> get() = accessor.actions.toList()

    val combatLevel get() = accessor.combatLevel

    val prayerIcon get() = accessor.prayerIcon

    val skullIcon get() = accessor.skullIcon

    val team get() = accessor.team

    val appearance: com.runesuite.client.game.api.Player.Appearance? get() = accessor.appearance?.let { com.runesuite.client.game.api.Player.Appearance(it) }

    override fun toString(): String {
        return "Player($name)"
    }

    class Appearance(override val accessor: XPlayerAppearance) : Wrapper() {

        val equipment: Map<com.runesuite.client.game.api.Player.Appearance.EquipmentSlot, Int>
            get() = accessor.equipment.withIndex()
                    .filter { com.runesuite.client.game.api.Player.Appearance.EquipmentSlot.Companion.LOOKUP.containsKey(it.index) }
                    .associate { com.runesuite.client.game.api.Player.Appearance.EquipmentSlot.Companion.LOOKUP[it.index]!! to it.value }

        val bodyColors: Map<com.runesuite.client.game.api.Player.Appearance.BodyPart, Int>
            get() = accessor.bodyColors.withIndex()
                    .associate { com.runesuite.client.game.api.Player.Appearance.BodyPart.Companion.LOOKUP[it.index]!! to it.value }

        val sex get() = if (accessor.isFemale) com.runesuite.client.game.api.Player.Appearance.Sex.FEMALE else com.runesuite.client.game.api.Player.Appearance.Sex.MALE

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
                @JvmField
                val LOOKUP = values().associateBy { it.id }
            }
        }
    }
}
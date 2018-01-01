package org.runestar.client.game.api

interface EquipmentSlot {

    val id: Int

    enum class Item(override val id: Int) : EquipmentSlot {
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
        FEET(10),
        // 11
        RING(12),
        AMMUNITION(13);

        companion object {
            @JvmField val LOOKUP = values().associateBy { it.id }
        }
    }

    enum class Kit(override val id: Int) : EquipmentSlot {
        ARMS(6),
        HAIR(8),
        JAW(11);

        companion object {
            @JvmField val LOOKUP = values().associateBy { it.id }
        }
    }

    companion object {
        // @JvmField
        val LOOKUP: Map<Int, EquipmentSlot> = Item.LOOKUP.plus(Kit.LOOKUP)
    }
}
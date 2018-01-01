package org.runestar.client.game.api

enum class EquipmentSlot(
        val id: Int,
        val supportsItems: Boolean
) {

    HEAD(0, true),
    CAPE(1, true),
    NECK(2, true),
    WEAPON(3, true),
    BODY(4, true),
    SHIELD(5, true),
    ARMS(6, false),
    LEGS(7, true),
    HAIR(8, false),
    HANDS(9, true),
    FEET(10, true),
    JAW(11, false),
    RING(12, true),
    AMMUNITION(13, false);

    companion object {
        @JvmField val VALUES = values().asList()
    }
}
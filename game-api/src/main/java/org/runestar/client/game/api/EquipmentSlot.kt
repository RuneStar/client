package org.runestar.client.game.api

enum class EquipmentSlot(
        val id: Int,
        val supportsItems: Boolean,
        val isVisible: Boolean
) {

    HEAD(0, true, true),
    CAPE(1, true, true),
    NECK(2, true, true),
    WEAPON(3, true, true),
    BODY(4, true, true),
    SHIELD(5, true, true),
    ARMS(6, false, true),
    LEGS(7, true, true),
    HAIR(8, false, true),
    HANDS(9, true, true),
    FEET(10, true, true),
    JAW(11, false, true),
    RING(12, true, false),
    AMMO(13, true, false);

    companion object {
        @JvmField val VALUES = values().asList()
    }
}
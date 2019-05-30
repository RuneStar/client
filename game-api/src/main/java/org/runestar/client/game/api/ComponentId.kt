package org.runestar.client.game.api

inline class ComponentId(val packed: Int) {

    constructor(group: Int, index: Int) : this((group shl 16) or index)

    val itf: Int get() = getItf(packed)

    val component: Int get() = getComponent(packed)

    override fun toString(): String = "ComponentId($itf:$component)"

    companion object {

        fun getItf(packed: Int) = packed shr 16

        fun getComponent(packed: Int) = packed and 0xFFFF

        val CASTLE_WARS_SARADOMIN_TIME_LEFT = ComponentId(InterfaceId.CASTLE_WARS_SARADOMIN, 25)

        val CASTLE_WARS_ZAMORAK_TIME_LEFT = ComponentId(InterfaceId.CASTLE_WARS_ZAMORAK, 25)

        val CHAT_BOX = ComponentId(InterfaceId.CHAT, 0)
        val CHAT_REPORT_TEXT = ComponentId(InterfaceId.CHAT, 36)

        val INVENTORY_ITEMS = ComponentId(InterfaceId.INVENTORY, 0)

        val MINIMAP_ORBS_HP_CIRCLE = ComponentId(InterfaceId.MINIMAP_ORBS, 6)
        val MINIMAP_ORBS_SPEC_CIRCLE = ComponentId(InterfaceId.MINIMAP_ORBS, 31)

        val COMBAT_OPTIONS_STYLE_1 = ComponentId(InterfaceId.COMBAT_OPTIONS, 4)
        val COMBAT_OPTIONS_STYLE_2 = ComponentId(InterfaceId.COMBAT_OPTIONS, 8)
        val COMBAT_OPTIONS_STYLE_3 = ComponentId(InterfaceId.COMBAT_OPTIONS, 12)
        val COMBAT_OPTIONS_STYLE_4 = ComponentId(InterfaceId.COMBAT_OPTIONS, 16)
        val COMBAT_OPTIONS_STYLE_DEFENSIVE_SPELL = ComponentId(InterfaceId.COMBAT_OPTIONS, 21)
        val COMBAT_OPTIONS_STYLE_DEFENSIVE_SPELL_2 = ComponentId(InterfaceId.COMBAT_OPTIONS, 22)
        val COMBAT_OPTIONS_STYLE_SPELL = ComponentId(InterfaceId.COMBAT_OPTIONS, 26)
        val COMBAT_OPTIONS_STYLE_SPELL_2 = ComponentId(InterfaceId.COMBAT_OPTIONS, 27)
    }
}
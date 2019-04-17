package org.runestar.client.game.api

inline class WidgetId(val packed: Int) {

    constructor(group: Int, index: Int) : this((group shl 16) or index)

    val group: Int get() = getGroup(packed)

    val index: Int get() = getIndex(packed)

    override fun toString(): String = "WidgetId($group:$index)"

    companion object {

        fun getGroup(packed: Int) = packed shr 16

        fun getIndex(packed: Int) = packed and 0xFFFF

        val CASTLE_WARS_SARADOMIN_TIME_LEFT = WidgetId(WidgetGroupId.CASTLE_WARS_SARADOMIN, 25)

        val CASTLE_WARS_ZAMORAK_TIME_LEFT = WidgetId(WidgetGroupId.CASTLE_WARS_ZAMORAK, 25)

        val CHAT_BOX = WidgetId(WidgetGroupId.CHAT, 0)
        val CHAT_REPORT_TEXT = WidgetId(WidgetGroupId.CHAT, 36)

        val INVENTORY_ITEMS = WidgetId(WidgetGroupId.INVENTORY, 0)

        val MINIMAP_ORBS_HP_CIRCLE = WidgetId(WidgetGroupId.MINIMAP_ORBS, 6)
        val MINIMAP_ORBS_SPEC_CIRCLE = WidgetId(WidgetGroupId.MINIMAP_ORBS, 31)

        val COMBAT_OPTIONS_STYLE_1 = WidgetId(WidgetGroupId.COMBAT_OPTIONS, 3)
        val COMBAT_OPTIONS_STYLE_2 = WidgetId(WidgetGroupId.COMBAT_OPTIONS, 7)
        val COMBAT_OPTIONS_STYLE_3 = WidgetId(WidgetGroupId.COMBAT_OPTIONS, 11)
        val COMBAT_OPTIONS_STYLE_4 = WidgetId(WidgetGroupId.COMBAT_OPTIONS, 15)
        val COMBAT_OPTIONS_STYLE_DEFENSIVE_SPELL = WidgetId(WidgetGroupId.COMBAT_OPTIONS, 20)
        val COMBAT_OPTIONS_STYLE_DEFENSIVE_SPELL_2 = WidgetId(WidgetGroupId.COMBAT_OPTIONS, 21)
        val COMBAT_OPTIONS_STYLE_SPELL = WidgetId(WidgetGroupId.COMBAT_OPTIONS, 25)
        val COMBAT_OPTIONS_STYLE_SPELL_2 = WidgetId(WidgetGroupId.COMBAT_OPTIONS, 26)
    }
}
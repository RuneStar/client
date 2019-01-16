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

        val CHAT_REPORT_TEXT = WidgetId(WidgetGroupId.CHAT, 36)

        val INVENTORY_ITEMS = WidgetId(WidgetGroupId.INVENTORY, 0)

        val MINIMAP_ORBS_HP_CIRCLE = WidgetId(WidgetGroupId.MINIMAP_ORBS, 6)
        val MINIMAP_ORBS_SPEC_CIRCLE = WidgetId(WidgetGroupId.MINIMAP_ORBS, 31)
    }
}
package com.runesuite.client.core.api

import java.awt.Point
import com.runesuite.client.core.raw.Client.accessor

interface MenuOption {
    val opcode: Int
    val operator: com.runesuite.client.core.api.MenuOption.Operator
    val argument0: Int
    val argument1: Int
    val argument2: Int
    val targetName: String
    val action: String

    enum class Operator(val opcode: Int) {
        OTHER_UNKNOWN(-1),

        ITEM_ON_OBJECT(1), //
        SPELL_ON_OBJECT(2), //
        OBJECT_ACTION_0(3), //
        OBJECT_ACTION_1(4), //
        OBJECT_ACTION_2(5), //
        OBJECT_ACTION_3(6), //
        ITEM_ON_NPC(7), //
        SPELL_ON_NPC(8), //
        NPC_ACTION_0(9), //
        NPC_ACTION_1(10), //
        NPC_ACTION_2(11), //
        NPC_ACTION_3(12), //
        NPC_ACTION_4(13), //
        ITEM_ON_PLAYER(14), //
        SPELL_ON_PLAYER(15), //
        ITEM_ON_GROUND_ITEM(16), //
        SPELL_ON_GROUND_ITEM(17), //
        GROUND_ITEM_ACTION_0(18), //
        GROUND_ITEM_ACTION_1(19), //
        GROUND_ITEM_ACTION_2(20), //
        GROUND_ITEM_ACTION_3(21), //
        GROUND_ITEM_ACTION_4(22), //
        WALK_HERE(23), //
        BUTTON_INPUT(24),
        BUTTON_SPELL(25), // use-spell
        BUTTON_CLOSE(26),
        // 27
        BUTTON_VARFLIP(28),
        BUTTON_VARSET(29),
        BUTTON_DIALOG(30), //
        ITEM_ON_ITEM(31), //
        SPELL_ON_ITEM(32),//
        ITEM_ACTION_0(33), //
        ITEM_ACTION_1(34), //
        ITEM_ACTION_2(35), //
        ITEM_ACTION_3(36), //
        ITEM_ACTION_4(37), //
        USE_ITEM(38), //
        WIDGET_ITEM_ACTION_0(39),
        WIDGET_ITEM_ACTION_1(40),
        WIDGET_ITEM_ACTION_2(41),
        WIDGET_ITEM_ACTION_3(42),
        WIDGET_ITEM_ACTION_4(43),
        PLAYER_ACTION_0(44), //
        PLAYER_ACTION_1(45), //
        PLAYER_ACTION_2(46), //
        PLAYER_ACTION_3(47), //
        PLAYER_ACTION_4(48), //
        PLAYER_ACTION_5(49), //
        PLAYER_ACTION_6(50), //
        PLAYER_ACTION_7(51), //
        // 52 - 56
        WIDGET_ACTION_0(57), //
        SPELL_ON_WIDGET(58),
        // 59 - 1000
        OBJECT_ACTION_4(1001), //
        OBJECT_EXAMINE(1002), //
        NPC_EXAMINE(1003), //
        GROUND_ITEM_EXAMINE(1004), //
        ITEM_EXAMINE(1005), //
        CANCEL(1006), //
        WIDGET_ACTION_1(1007); //

        companion object {
            val LOOKUP = values().associateBy { it.opcode }
            fun fromOpcode(opcode: Int): com.runesuite.client.core.api.MenuOption.Operator {
                val fixed = if (opcode >= 2000) opcode - 2000 else opcode
                return com.runesuite.client.core.api.MenuOption.Operator.Companion.LOOKUP[fixed] ?: com.runesuite.client.core.api.MenuOption.Operator.OTHER_UNKNOWN
            }
        }
    }

    private data class Base(
            override val opcode: Int,
            override val operator: com.runesuite.client.core.api.MenuOption.Operator,
            override val argument0: Int,
            override val argument1: Int,
            override val argument2: Int,
            override val targetName: String,
            override val action: String
    ) : com.runesuite.client.core.api.MenuOption

    companion object {
        fun of(opcode: Int, argument0: Int, argument1: Int, argument2: Int, targetName: String, action: String): com.runesuite.client.core.api.MenuOption {
            val operator = com.runesuite.client.core.api.MenuOption.Operator.Companion.fromOpcode(opcode)
            val base = com.runesuite.client.core.api.MenuOption.Base(opcode, operator, argument0, argument1, argument2, targetName, action)
            return when(operator) {
                // todo: subclasses
                // todo: Object Examine is wrong
                in com.runesuite.client.core.api.MenuOption.OnObjectIndexed.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.OnObjectIndexed(base)
                in com.runesuite.client.core.api.MenuOption.Cancel.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.Cancel(base)
                in com.runesuite.client.core.api.MenuOption.WalkHere.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.WalkHere(base)
                in com.runesuite.client.core.api.MenuOption.OnNpcIndexed.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.OnNpcIndexed(base)
                in com.runesuite.client.core.api.MenuOption.OnPlayerIndexed.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.OnPlayerIndexed(base)
                in com.runesuite.client.core.api.MenuOption.OnNpcSimple.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.OnNpcSimple(base)
                in com.runesuite.client.core.api.MenuOption.OnPlayerSimple.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.OnPlayerSimple(base)
                in com.runesuite.client.core.api.MenuOption.OnObjectSimple.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.OnObjectSimple(base)
                in com.runesuite.client.core.api.MenuOption.OnGroundItemIndexed.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.OnGroundItemIndexed(base)
                in com.runesuite.client.core.api.MenuOption.OnGroundItemSimple.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.OnGroundItemSimple(base)
                in com.runesuite.client.core.api.MenuOption.OnItemIndexed.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.OnItemIndexed(base)
                in com.runesuite.client.core.api.MenuOption.OnItemSimple.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.OnItemSimple(base)
                in com.runesuite.client.core.api.MenuOption.OnWidgetSimple.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.OnWidgetSimple(base)
                in com.runesuite.client.core.api.MenuOption.ButtonDialog.Companion.OPERATORS -> com.runesuite.client.core.api.MenuOption.ButtonDialog(base)
                else -> base
            }
        }
    }

    interface AtLocation : com.runesuite.client.core.api.MenuOption {
        val plane: Int
        val location: com.runesuite.client.core.api.SceneTile get() = com.runesuite.client.core.api.SceneTile(argument1, argument2, plane)
    }

    interface Indexed : com.runesuite.client.core.api.MenuOption {
        val index: Int
    }

    interface OnScreen : com.runesuite.client.core.api.MenuOption {
        val screenLocation get() = Point(argument1, argument2)
    }

    interface OnObject: com.runesuite.client.core.api.MenuOption {
        val tag: com.runesuite.client.core.api.EntityTag get() = com.runesuite.client.core.api.EntityTag(argument0)
    }

    interface OnGroundItem: com.runesuite.client.core.api.MenuOption {
        val id get() = argument0
    }

    interface OnItem: com.runesuite.client.core.api.MenuOption, com.runesuite.client.core.api.MenuOption.InWidget {
        val id get() = argument0
        val slot get() = argument1
    }

    interface InWidget: com.runesuite.client.core.api.MenuOption {
        val group get() = com.runesuite.client.core.api.WidgetGroup(argument2 shr 16)
        val widgetId get() = argument2 and 0xFFFF
        val widget: com.runesuite.client.core.api.Widget? get() = group[widgetId]
    }

    interface OnWidget: com.runesuite.client.core.api.MenuOption, com.runesuite.client.core.api.MenuOption.InWidget {
        val widgetChildId: Int? get() = argument1.let { if (it == -1) null else it }
        val widgetParent: com.runesuite.client.core.api.Widget? get() = super.widget
        override val widget: com.runesuite.client.core.api.Widget? get() = widgetParent?.let { p -> widgetChildId?.let { c -> p[c] } ?: p }
    }

    data class OnWidgetSimple(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.OnWidget {
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.OnWidgetSimple.Companion.OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(com.runesuite.client.core.api.MenuOption.Operator.WIDGET_ACTION_0, com.runesuite.client.core.api.MenuOption.Operator.WIDGET_ACTION_1, com.runesuite.client.core.api.MenuOption.Operator.BUTTON_SPELL)
        }
        override fun toString(): String {
            return "OnWidgetSimple(opcode=$opcode, operator=$operator, widgetId=$widgetId, widgetChildId=$widgetChildId, targetName=$targetName, action=$action)"
        }
    }

    data class ButtonDialog(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.InWidget, com.runesuite.client.core.api.MenuOption.Indexed {
        override val index get() = argument1
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.ButtonDialog.Companion.OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(com.runesuite.client.core.api.MenuOption.Operator.BUTTON_DIALOG)
        }
        override fun toString(): String {
            return "ButtonDialog(opcode=$opcode, operator=$operator, index=$index, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnItemIndexed(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.Indexed, com.runesuite.client.core.api.MenuOption.OnItem {
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.OnItemIndexed.Companion.OPERATORS) { operator }
        }
        override val index get() = opcode - com.runesuite.client.core.api.MenuOption.Operator.ITEM_ACTION_0.opcode
        companion object {
            val OPERATORS = setOf(
                    com.runesuite.client.core.api.MenuOption.Operator.ITEM_ACTION_0, com.runesuite.client.core.api.MenuOption.Operator.ITEM_ACTION_1, com.runesuite.client.core.api.MenuOption.Operator.ITEM_ACTION_2,
                    com.runesuite.client.core.api.MenuOption.Operator.ITEM_ACTION_3, com.runesuite.client.core.api.MenuOption.Operator.ITEM_ACTION_4)
        }
        override fun toString(): String {
            return "OnItemIndexed(opcode=$opcode, operator=$operator, index=$index, id=$id, slot=$slot, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnItemSimple(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.OnItem {
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.OnItemSimple.Companion.OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(
                    com.runesuite.client.core.api.MenuOption.Operator.ITEM_EXAMINE, com.runesuite.client.core.api.MenuOption.Operator.SPELL_ON_ITEM, com.runesuite.client.core.api.MenuOption.Operator.ITEM_ON_ITEM,
                    com.runesuite.client.core.api.MenuOption.Operator.USE_ITEM)
        }
        override fun toString(): String {
            return "OnItemSimple(opcode=$opcode, operator=$operator, id=$id, slot=$slot, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnGroundItemIndexed(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.Indexed, com.runesuite.client.core.api.MenuOption.OnGroundItem, com.runesuite.client.core.api.MenuOption.AtLocation {
        override val plane: Int = accessor.plane
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.OnGroundItemIndexed.Companion.OPERATORS) { operator }
        }
        override val index: Int get() = opcode - com.runesuite.client.core.api.MenuOption.Operator.GROUND_ITEM_ACTION_0.opcode
        companion object {
            val OPERATORS = setOf(
                    com.runesuite.client.core.api.MenuOption.Operator.GROUND_ITEM_ACTION_0, com.runesuite.client.core.api.MenuOption.Operator.GROUND_ITEM_ACTION_1, com.runesuite.client.core.api.MenuOption.Operator.GROUND_ITEM_ACTION_2,
                    com.runesuite.client.core.api.MenuOption.Operator.GROUND_ITEM_ACTION_3, com.runesuite.client.core.api.MenuOption.Operator.GROUND_ITEM_ACTION_4)
        }
        override fun toString(): String {
            return "OnGroundItemIndexed(opcode=$opcode, operator=$operator, index=$index, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }

    data class OnGroundItemSimple(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.OnGroundItem, com.runesuite.client.core.api.MenuOption.AtLocation {
        override val plane: Int = accessor.plane
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.OnGroundItemSimple.Companion.OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(com.runesuite.client.core.api.MenuOption.Operator.ITEM_ON_GROUND_ITEM, com.runesuite.client.core.api.MenuOption.Operator.GROUND_ITEM_EXAMINE, com.runesuite.client.core.api.MenuOption.Operator.SPELL_ON_GROUND_ITEM)
        }
        override fun toString(): String {
            return "OnGroundItemSimple(opcode=$opcode, operator=$operator, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }

    data class Cancel(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption {
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.Cancel.Companion.OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(com.runesuite.client.core.api.MenuOption.Operator.CANCEL)
        }
        override fun toString(): String {
            return "Cancel(opcode=$opcode, operator=$operator, action=$action)"
        }
    }

    data class WalkHere(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.OnScreen {
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.WalkHere.Companion.OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(com.runesuite.client.core.api.MenuOption.Operator.WALK_HERE)
        }
        override fun toString(): String {
            return "WalkHere(opcode=$opcode, operator=$operator, screenLocation=$screenLocation, action=$action)"
        }
    }

    data class OnObjectIndexed(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.AtLocation, com.runesuite.client.core.api.MenuOption.Indexed, com.runesuite.client.core.api.MenuOption.OnObject {
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.OnObjectIndexed.Companion.OPERATORS) { operator }
        }
        override val plane: Int = accessor.plane
        override val index: Int get() = opcode - com.runesuite.client.core.api.MenuOption.Operator.OBJECT_ACTION_0.opcode
        companion object {
            val OPERATORS = setOf(
                    com.runesuite.client.core.api.MenuOption.Operator.OBJECT_ACTION_0, com.runesuite.client.core.api.MenuOption.Operator.OBJECT_ACTION_1, com.runesuite.client.core.api.MenuOption.Operator.OBJECT_ACTION_2,
                    com.runesuite.client.core.api.MenuOption.Operator.OBJECT_ACTION_3, com.runesuite.client.core.api.MenuOption.Operator.OBJECT_ACTION_4)
        }
        override fun toString(): String {
            return "OnObjectIndexed(opcode=$opcode, operator=$operator, index=$index, tag=$tag, targetName=$targetName, action=$action)"
        }
    }

    interface OnNpc : com.runesuite.client.core.api.MenuOption, com.runesuite.client.core.api.ActorTargeting {
        override val playerTargetIndex: Int? get() = null
        override val npcTargetIndex: Int get() = argument0
        override val target: com.runesuite.client.core.api.Npc? get() = super.target as com.runesuite.client.core.api.Npc?
    }

    interface OnPlayer : com.runesuite.client.core.api.MenuOption, com.runesuite.client.core.api.ActorTargeting {
        override val playerTargetIndex: Int get() = argument0
        override val npcTargetIndex: Int? get() = null
        override val target: com.runesuite.client.core.api.Player? get() = super.target as com.runesuite.client.core.api.Player?
    }

    data class OnNpcIndexed(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.Indexed, com.runesuite.client.core.api.MenuOption.OnNpc {
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.OnNpcIndexed.Companion.OPERATORS) { operator }
        }
        override val index: Int get() = opcode - com.runesuite.client.core.api.MenuOption.Operator.NPC_ACTION_0.opcode
        companion object {
            val OPERATORS = setOf(
                    com.runesuite.client.core.api.MenuOption.Operator.NPC_ACTION_0, com.runesuite.client.core.api.MenuOption.Operator.NPC_ACTION_1, com.runesuite.client.core.api.MenuOption.Operator.NPC_ACTION_2,
                    com.runesuite.client.core.api.MenuOption.Operator.NPC_ACTION_3, com.runesuite.client.core.api.MenuOption.Operator.NPC_ACTION_4)
        }
        override fun toString(): String {
            return "OnNpcIndexed(opcode=$opcode, operator=$operator, index=$index, npcTargetIndex=$npcTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerIndexed(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.Indexed, com.runesuite.client.core.api.MenuOption.OnPlayer {
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.OnPlayerIndexed.Companion.OPERATORS) { operator }
        }
        override val index: Int get() = opcode - com.runesuite.client.core.api.MenuOption.Operator.PLAYER_ACTION_0.opcode
        companion object {
            val OPERATORS = setOf(
                    com.runesuite.client.core.api.MenuOption.Operator.PLAYER_ACTION_0, com.runesuite.client.core.api.MenuOption.Operator.PLAYER_ACTION_1, com.runesuite.client.core.api.MenuOption.Operator.PLAYER_ACTION_2,
                    com.runesuite.client.core.api.MenuOption.Operator.PLAYER_ACTION_3, com.runesuite.client.core.api.MenuOption.Operator.PLAYER_ACTION_4, com.runesuite.client.core.api.MenuOption.Operator.PLAYER_ACTION_5,
                    com.runesuite.client.core.api.MenuOption.Operator.PLAYER_ACTION_6, com.runesuite.client.core.api.MenuOption.Operator.PLAYER_ACTION_7)
        }
        override fun toString(): String {
            return "onPlayerIndexed(opcode=$opcode, operator=$operator, index=$index, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnNpcSimple(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.OnNpc {
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.OnNpcSimple.Companion.OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(com.runesuite.client.core.api.MenuOption.Operator.SPELL_ON_NPC, com.runesuite.client.core.api.MenuOption.Operator.ITEM_ON_NPC, com.runesuite.client.core.api.MenuOption.Operator.NPC_EXAMINE)
        }
        override fun toString(): String {
            return "OnNpcSimple(opcode=$opcode, operator=$operator, npcTargetIndex=$npcTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerSimple(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.OnPlayer {
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.OnPlayerSimple.Companion.OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(com.runesuite.client.core.api.MenuOption.Operator.SPELL_ON_PLAYER, com.runesuite.client.core.api.MenuOption.Operator.ITEM_ON_PLAYER)
        }
        override fun toString(): String {
            return "OnPlayerSimple(opcode=$opcode, operator=$operator, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnObjectSimple(private val menuOption: com.runesuite.client.core.api.MenuOption) : com.runesuite.client.core.api.MenuOption by menuOption, com.runesuite.client.core.api.MenuOption.OnObject, com.runesuite.client.core.api.MenuOption.AtLocation {
        init {
            require(operator in com.runesuite.client.core.api.MenuOption.OnObjectSimple.Companion.OPERATORS) { operator }
        }
        override val plane: Int = accessor.plane
        companion object {
            val OPERATORS = setOf(com.runesuite.client.core.api.MenuOption.Operator.SPELL_ON_OBJECT, com.runesuite.client.core.api.MenuOption.Operator.OBJECT_EXAMINE, com.runesuite.client.core.api.MenuOption.Operator.ITEM_ON_OBJECT)
        }
        override fun toString(): String {
            return "OnObjectSimple(opcode=$opcode, operator=$operator, tag=$tag, location=$location, targetName=$targetName, action=$action)"
        }
    }
}

package com.runesuite.client.game

import com.runesuite.client.base.Client.accessor
import java.awt.Point

interface MenuOption {
    val opcode: Int
    val operator: Operator
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
            fun fromOpcode(opcode: Int): Operator {
                val fixed = if (opcode >= 2000) opcode - 2000 else opcode
                return LOOKUP[fixed] ?: OTHER_UNKNOWN
            }
        }
    }

    private data class Base(
            override val opcode: Int,
            override val operator: Operator,
            override val argument0: Int,
            override val argument1: Int,
            override val argument2: Int,
            override val targetName: String,
            override val action: String
    ) : MenuOption

    companion object {
        fun of(opcode: Int, argument0: Int, argument1: Int, argument2: Int, targetName: String, action: String): MenuOption {
            val operator = Operator.fromOpcode(opcode)
            val base = Base(opcode, operator, argument0, argument1, argument2, targetName, action)
            return when(operator) {
                // todo: subclasses
                in OnObjectIndexed.OPERATORS -> OnObjectIndexed(base)
                in Cancel.OPERATORS -> Cancel(base)
                in WalkHere.OPERATORS -> WalkHere(base)
                in OnNpcIndexed.OPERATORS -> OnNpcIndexed(base)
                in OnPlayerIndexed.OPERATORS -> OnPlayerIndexed(base)
                in OnNpcSimple.OPERATORS -> OnNpcSimple(base)
                in OnPlayerSimple.OPERATORS -> OnPlayerSimple(base)
                in OnObjectSimple.OPERATORS -> OnObjectSimple(base)
                in OnGroundItemIndexed.OPERATORS -> OnGroundItemIndexed(base)
                in OnGroundItemSimple.OPERATORS -> OnGroundItemSimple(base)
                in OnItemIndexed.OPERATORS -> OnItemIndexed(base)
                in OnItemSimple.OPERATORS -> OnItemSimple(base)
                in OnWidgetSimple.OPERATORS -> OnWidgetSimple(base)
                in ButtonDialog.OPERATORS -> ButtonDialog(base)
                else -> base
            }
        }
    }

    interface AtLocation : MenuOption {
        val plane: Int
        val location: SceneTile get() = SceneTile(argument1, argument2, plane)
    }

    interface Indexed : MenuOption {
        val index: Int
    }

    interface OnScreen : MenuOption {
        val screenLocation get() = Point(argument1, argument2)
    }

    interface OnObject: MenuOption {
        val uid: InteractableUid get() = InteractableUid(argument0)
    }

    interface OnGroundItem: MenuOption {
        val id get() = argument0
    }

    interface OnItem: MenuOption, InWidget {
        val id get() = argument0
        val slot get() = argument1
    }

    interface InWidget: MenuOption {
        val group get() = WidgetGroup(argument2 shr 16)
        val widgetId get() = argument2 and 0xFFFF
        val widget: Widget? get() = group[widgetId]
    }

    interface OnWidget: MenuOption, InWidget {
        val widgetChildId: Int? get() = argument1.let { if (it == -1) null else it }
        val widgetParent: Widget? get() = super.widget
        override val widget: Widget? get() = widgetParent?.let { p -> widgetChildId?.let { c -> p[c] } ?: p }
    }

    data class OnWidgetSimple(private val menuOption: MenuOption) : MenuOption by menuOption, OnWidget {
        init {
            require(operator in OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(Operator.WIDGET_ACTION_0, Operator.WIDGET_ACTION_1, Operator.BUTTON_SPELL)
        }
        override fun toString(): String {
            return "OnWidgetSimple(opcode=$opcode, operator=$operator, widgetId=$widgetId, widgetChildId=$widgetChildId, targetName=$targetName, action=$action)"
        }
    }

    data class ButtonDialog(private val menuOption: MenuOption) : MenuOption by menuOption, InWidget, Indexed {
        override val index get() = argument1
        init {
            require(operator in OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(Operator.BUTTON_DIALOG)
        }
        override fun toString(): String {
            return "ButtonDialog(opcode=$opcode, operator=$operator, index=$index, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnItemIndexed(private val menuOption: MenuOption) : MenuOption by menuOption, Indexed, OnItem {
        init {
            require(operator in OPERATORS) { operator }
        }
        override val index get() = opcode - Operator.ITEM_ACTION_0.opcode
        companion object {
            val OPERATORS = setOf(
                    Operator.ITEM_ACTION_0, Operator.ITEM_ACTION_1, Operator.ITEM_ACTION_2,
                    Operator.ITEM_ACTION_3, Operator.ITEM_ACTION_4)
        }
        override fun toString(): String {
            return "OnItemIndexed(opcode=$opcode, operator=$operator, index=$index, id=$id, slot=$slot, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnItemSimple(private val menuOption: MenuOption) : MenuOption by menuOption, OnItem {
        init {
            require(operator in OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(
                    Operator.ITEM_EXAMINE, Operator.SPELL_ON_ITEM, Operator.ITEM_ON_ITEM,
                    Operator.USE_ITEM)
        }
        override fun toString(): String {
            return "OnItemSimple(opcode=$opcode, operator=$operator, id=$id, slot=$slot, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnGroundItemIndexed(private val menuOption: MenuOption) : MenuOption by menuOption, Indexed, OnGroundItem, AtLocation {
        override val plane: Int = accessor.plane
        init {
            require(operator in OPERATORS) { operator }
        }
        override val index: Int get() = opcode - Operator.GROUND_ITEM_ACTION_0.opcode
        companion object {
            val OPERATORS = setOf(
                    Operator.GROUND_ITEM_ACTION_0, Operator.GROUND_ITEM_ACTION_1, Operator.GROUND_ITEM_ACTION_2,
                    Operator.GROUND_ITEM_ACTION_3, Operator.GROUND_ITEM_ACTION_4)
        }
        override fun toString(): String {
            return "OnGroundItemIndexed(opcode=$opcode, operator=$operator, index=$index, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }

    data class OnGroundItemSimple(private val menuOption: MenuOption) : MenuOption by menuOption, OnGroundItem, AtLocation {
        override val plane: Int = accessor.plane
        init {
            require(operator in OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(Operator.ITEM_ON_GROUND_ITEM, Operator.GROUND_ITEM_EXAMINE, Operator.SPELL_ON_GROUND_ITEM)
        }
        override fun toString(): String {
            return "OnGroundItemSimple(opcode=$opcode, operator=$operator, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }

    data class Cancel(private val menuOption: MenuOption) : MenuOption by menuOption {
        init {
            require(operator in OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(Operator.CANCEL)
        }
        override fun toString(): String {
            return "Cancel(opcode=$opcode, operator=$operator, action=$action)"
        }
    }

    data class WalkHere(private val menuOption: MenuOption) : MenuOption by menuOption, OnScreen {
        init {
            require(operator in OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(Operator.WALK_HERE)
        }
        override fun toString(): String {
            return "WalkHere(opcode=$opcode, operator=$operator, screenLocation=$screenLocation, action=$action)"
        }
    }

    data class OnObjectIndexed(private val menuOption: MenuOption) : MenuOption by menuOption, AtLocation, Indexed, OnObject {
        init {
            require(operator in OPERATORS) { operator }
        }
        override val plane: Int = accessor.plane
        override val index: Int get() = opcode - Operator.OBJECT_ACTION_0.opcode
        companion object {
            val OPERATORS = setOf(
                    Operator.OBJECT_ACTION_0, Operator.OBJECT_ACTION_1, Operator.OBJECT_ACTION_2,
                    Operator.OBJECT_ACTION_3, Operator.OBJECT_ACTION_4)
        }
        override fun toString(): String {
            return "OnObjectIndexed(opcode=$opcode, operator=$operator, index=$index, uid=$uid, targetName=$targetName, action=$action)"
        }
    }

    interface OnNpc : MenuOption, ActorTargeting {
        override val playerTargetIndex: Int? get() = null
        override val npcTargetIndex: Int get() = argument0
        override val target: Npc? get() = super.target as Npc?
    }

    interface OnPlayer : MenuOption, ActorTargeting {
        override val playerTargetIndex: Int get() = argument0
        override val npcTargetIndex: Int? get() = null
        override val target: Player? get() = super.target as Player?
    }

    data class OnNpcIndexed(private val menuOption: MenuOption) : MenuOption by menuOption, Indexed, OnNpc {
        init {
            require(operator in OPERATORS) { operator }
        }
        override val index: Int get() = opcode - Operator.NPC_ACTION_0.opcode
        companion object {
            val OPERATORS = setOf(
                    Operator.NPC_ACTION_0, Operator.NPC_ACTION_1, Operator.NPC_ACTION_2,
                    Operator.NPC_ACTION_3, Operator.NPC_ACTION_4)
        }
        override fun toString(): String {
            return "OnNpcIndexed(opcode=$opcode, operator=$operator, index=$index, npcTargetIndex=$npcTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerIndexed(private val menuOption: MenuOption) : MenuOption by menuOption, Indexed, OnPlayer {
        init {
            require(operator in OPERATORS) { operator }
        }
        override val index: Int get() = opcode - Operator.PLAYER_ACTION_0.opcode
        companion object {
            val OPERATORS = setOf(
                    Operator.PLAYER_ACTION_0, Operator.PLAYER_ACTION_1, Operator.PLAYER_ACTION_2,
                    Operator.PLAYER_ACTION_3, Operator.PLAYER_ACTION_4, Operator.PLAYER_ACTION_5,
                    Operator.PLAYER_ACTION_6, Operator.PLAYER_ACTION_7)
        }
        override fun toString(): String {
            return "onPlayerIndexed(opcode=$opcode, operator=$operator, index=$index, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnNpcSimple(private val menuOption: MenuOption) : MenuOption by menuOption, OnNpc {
        init {
            require(operator in OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(Operator.SPELL_ON_NPC, Operator.ITEM_ON_NPC, Operator.NPC_EXAMINE)
        }
        override fun toString(): String {
            return "OnNpcSimple(opcode=$opcode, operator=$operator, npcTargetIndex=$npcTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerSimple(private val menuOption: MenuOption) : MenuOption by menuOption, OnPlayer {
        init {
            require(operator in OPERATORS) { operator }
        }
        companion object {
            val OPERATORS = setOf(Operator.SPELL_ON_PLAYER, Operator.ITEM_ON_PLAYER)
        }
        override fun toString(): String {
            return "OnPlayerSimple(opcode=$opcode, operator=$operator, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnObjectSimple(private val menuOption: MenuOption) : MenuOption by menuOption, OnObject, AtLocation {
        init {
            require(operator in OPERATORS) { operator }
        }
        override val plane: Int = accessor.plane
        companion object {
            val OPERATORS = setOf(Operator.SPELL_ON_OBJECT, Operator.OBJECT_EXAMINE, Operator.ITEM_ON_OBJECT)
        }
        override fun toString(): String {
            return "OnObjectSimple(opcode=$opcode, operator=$operator, uid=$uid, location=$location, targetName=$targetName, action=$action)"
        }
    }
}

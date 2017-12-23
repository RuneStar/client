package org.runestar.client.game.api

import org.runestar.client.game.api.live.Widgets
import org.runestar.client.game.raw.Client.accessor
import java.awt.Point

interface MenuOption {

    val opcode: Int
    val operator: MenuOption.Operator
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

            @JvmField
            val LOOKUP = values().associateBy { it.opcode }

            @JvmStatic
            fun fromOpcode(opcode: Int): MenuOption.Operator {
                val fixed = if (opcode >= 2000) opcode - 2000 else opcode
                return MenuOption.Operator.LOOKUP[fixed] ?: MenuOption.Operator.OTHER_UNKNOWN
            }
        }
    }

    private data class Base(
            override val opcode: Int,
            override val operator: MenuOption.Operator,
            override val argument0: Int,
            override val argument1: Int,
            override val argument2: Int,
            override val targetName: String,
            override val action: String
    ) : MenuOption

    companion object {

        fun of(opcode: Int, argument0: Int, argument1: Int, argument2: Int, targetName: String, action: String): MenuOption {
            val operator = MenuOption.Operator.fromOpcode(opcode)
            val base = MenuOption.Base(opcode, operator, argument0, argument1, argument2, targetName, action)
            return when(operator) {
                // todo: subclasses
                // todo: Object Examine is wrong
                in MenuOption.OnObjectIndexed.operators -> MenuOption.OnObjectIndexed(base)
                in MenuOption.Cancel.operators -> MenuOption.Cancel(base)
                in MenuOption.WalkHere.operators -> MenuOption.WalkHere(base)
                in MenuOption.OnNpcIndexed.operators -> MenuOption.OnNpcIndexed(base)
                in MenuOption.OnPlayerIndexed.operators -> MenuOption.OnPlayerIndexed(base)
                in MenuOption.OnNpcSimple.operators -> MenuOption.OnNpcSimple(base)
                in MenuOption.OnPlayerSimple.operators -> MenuOption.OnPlayerSimple(base)
                in MenuOption.OnObjectSimple.operators -> MenuOption.OnObjectSimple(base)
                in MenuOption.OnGroundItemIndexed.operators -> MenuOption.OnGroundItemIndexed(base)
                in MenuOption.OnGroundItemSimple.operators -> MenuOption.OnGroundItemSimple(base)
                in MenuOption.OnItemIndexed.operators -> MenuOption.OnItemIndexed(base)
                in MenuOption.OnItemSimple.operators -> MenuOption.OnItemSimple(base)
                in MenuOption.OnWidgetSimple.operators -> MenuOption.OnWidgetSimple(base)
                in MenuOption.ButtonDialog.operators -> MenuOption.ButtonDialog(base)
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
        val tag: EntityTag get() = EntityTag(argument0)
    }

    interface OnGroundItem: MenuOption {
        val id get() = argument0
    }

    interface OnItem: MenuOption, MenuOption.InWidget {
        val id get() = argument0
        val slot get() = argument1
    }

    interface InWidget: MenuOption {
        val widgetId get() = WidgetParentId(argument2)
        val widgetParent: Widget.Parent get() = checkNotNull(Widgets[widgetId])
    }

    interface OnWidget: MenuOption, MenuOption.InWidget {
        val widgetChildId: Int? get() = argument1.let { if (it == -1) null else it }
        val widget: Widget? get() = widgetParent.let { p -> widgetChildId?.let { c -> p[c] } ?: p }
    }

    interface Factory {
        val operators: Set<Operator>
        fun check(operator: Operator) {
            require(operator in operators) { operator }
        }
    }

    data class OnWidgetSimple(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.OnWidget {
        init {
            check(operator)
        }
        companion object : Factory {
            override val operators = setOf(Operator.WIDGET_ACTION_0, Operator.WIDGET_ACTION_1, Operator.BUTTON_SPELL)
        }
        override fun toString(): String {
            return "OnWidgetSimple(opcode=$opcode, operator=$operator, widgetId=$widgetId, widgetChildId=$widgetChildId, targetName=$targetName, action=$action)"
        }
    }

    data class ButtonDialog(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.InWidget, MenuOption.Indexed {
        override val index get() = argument1
        init {
            check(operator)
        }
        companion object : Factory {
            override val operators = setOf(Operator.BUTTON_DIALOG)
        }
        override fun toString(): String {
            return "ButtonDialog(opcode=$opcode, operator=$operator, index=$index, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnItemIndexed(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.Indexed, MenuOption.OnItem {
        init {
            check(operator)
        }
        override val index get() = opcode - Operator.ITEM_ACTION_0.opcode
        companion object : Factory {
            override val operators = setOf(Operator.ITEM_ACTION_0, Operator.ITEM_ACTION_1, Operator.ITEM_ACTION_2, Operator.ITEM_ACTION_3, Operator.ITEM_ACTION_4)
        }
        override fun toString(): String {
            return "OnItemIndexed(opcode=$opcode, operator=$operator, index=$index, id=$id, slot=$slot, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnItemSimple(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.OnItem {
        init {
            check(operator)
        }
        companion object : Factory {
            override val operators = setOf(Operator.ITEM_EXAMINE, Operator.SPELL_ON_ITEM, Operator.ITEM_ON_ITEM, Operator.USE_ITEM)
        }
        override fun toString(): String {
            return "OnItemSimple(opcode=$opcode, operator=$operator, id=$id, slot=$slot, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnGroundItemIndexed(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.Indexed, MenuOption.OnGroundItem, MenuOption.AtLocation {
        override val plane: Int = accessor.plane
        init {
            check(operator)
        }
        override val index: Int get() = opcode - Operator.GROUND_ITEM_ACTION_0.opcode
        companion object : Factory {
            override val operators = setOf(
                    Operator.GROUND_ITEM_ACTION_0, Operator.GROUND_ITEM_ACTION_1, Operator.GROUND_ITEM_ACTION_2,
                    Operator.GROUND_ITEM_ACTION_3, Operator.GROUND_ITEM_ACTION_4
            )
        }
        override fun toString(): String {
            return "OnGroundItemIndexed(opcode=$opcode, operator=$operator, index=$index, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }

    data class OnGroundItemSimple(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.OnGroundItem, MenuOption.AtLocation {
        override val plane: Int = accessor.plane
        init {
            check(operator)
        }
        companion object : Factory {
            override val operators = setOf(Operator.ITEM_ON_GROUND_ITEM, Operator.GROUND_ITEM_EXAMINE, Operator.SPELL_ON_GROUND_ITEM)
        }
        override fun toString(): String {
            return "OnGroundItemSimple(opcode=$opcode, operator=$operator, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }

    data class Cancel(private val menuOption: MenuOption) : MenuOption by menuOption {
        init {
            check(operator)
        }
        companion object : Factory {
            override val operators = setOf(Operator.CANCEL)
        }
        override fun toString(): String {
            return "Cancel(opcode=$opcode, operator=$operator, action=$action)"
        }
    }

    data class WalkHere(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.OnScreen {
        init {
            check(operator)
        }
        companion object : Factory {
            override val operators = setOf(Operator.WALK_HERE)
        }
        override fun toString(): String {
            return "WalkHere(opcode=$opcode, operator=$operator, screenLocation=$screenLocation, action=$action)"
        }
    }

    data class OnObjectIndexed(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.AtLocation, MenuOption.Indexed, MenuOption.OnObject {
        init {
            check(operator)
        }
        override val plane: Int = accessor.plane
        override val index: Int get() = opcode - Operator.OBJECT_ACTION_0.opcode
        companion object : Factory {
            override val operators = setOf(
                    Operator.OBJECT_ACTION_0, Operator.OBJECT_ACTION_1, Operator.OBJECT_ACTION_2,
                    Operator.OBJECT_ACTION_3, Operator.OBJECT_ACTION_4
            )
        }
        override fun toString(): String {
            return "OnObjectIndexed(opcode=$opcode, operator=$operator, index=$index, tag=$tag, targetName=$targetName, action=$action)"
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

    data class OnNpcIndexed(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.Indexed, MenuOption.OnNpc {
        init {
            check(operator)
        }
        override val index: Int get() = opcode - MenuOption.Operator.NPC_ACTION_0.opcode
        companion object : Factory {
            override val operators = setOf(
                    Operator.NPC_ACTION_0, Operator.NPC_ACTION_1, Operator.NPC_ACTION_2,
                    Operator.NPC_ACTION_3, Operator.NPC_ACTION_4
            )
        }
        override fun toString(): String {
            return "OnNpcIndexed(opcode=$opcode, operator=$operator, index=$index, npcTargetIndex=$npcTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerIndexed(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.Indexed, MenuOption.OnPlayer {
        init {
            check(operator)
        }
        override val index: Int get() = opcode - MenuOption.Operator.PLAYER_ACTION_0.opcode
        companion object : Factory {
            override val operators = setOf(
                    Operator.PLAYER_ACTION_0, Operator.PLAYER_ACTION_1, Operator.PLAYER_ACTION_2,
                    Operator.PLAYER_ACTION_3, Operator.PLAYER_ACTION_4, Operator.PLAYER_ACTION_5,
                    Operator.PLAYER_ACTION_6, Operator.PLAYER_ACTION_7)
        }
        override fun toString(): String {
            return "OnPlayerIndexed(opcode=$opcode, operator=$operator, index=$index, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnNpcSimple(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.OnNpc {
        init {
            check(operator)
        }
        companion object : Factory {
            override val operators = setOf(Operator.SPELL_ON_NPC, Operator.ITEM_ON_NPC, Operator.NPC_EXAMINE)
        }
        override fun toString(): String {
            return "OnNpcSimple(opcode=$opcode, operator=$operator, npcTargetIndex=$npcTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerSimple(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.OnPlayer {
        init {
            check(operator)
        }
        companion object : Factory {
            override val operators = setOf(Operator.SPELL_ON_PLAYER, Operator.ITEM_ON_PLAYER)
        }
        override fun toString(): String {
            return "OnPlayerSimple(opcode=$opcode, operator=$operator, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnObjectSimple(private val menuOption: MenuOption) : MenuOption by menuOption, MenuOption.OnObject, MenuOption.AtLocation {
        init {
            check(operator)
        }
        override val plane: Int = accessor.plane
        companion object : Factory {
            override val operators = setOf(Operator.SPELL_ON_OBJECT, Operator.OBJECT_EXAMINE, Operator.ITEM_ON_OBJECT)
        }
        override fun toString(): String {
            return "OnObjectSimple(opcode=$opcode, operator=$operator, tag=$tag, location=$location, targetName=$targetName, action=$action)"
        }
    }
}

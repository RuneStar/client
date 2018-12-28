package org.runestar.client.game.api

import org.runestar.client.game.api.MenuOptionOpcode.*
import org.runestar.client.game.api.live.Widgets
import org.runestar.client.game.raw.CLIENT
import java.awt.Point

interface MenuOption {

    val opcode: Int
    val argument0: Int
    val argument1: Int
    val argument2: Int
    val targetName: String
    val action: String
    val shiftClick: Boolean

    private data class Base(
            override val opcode: Int,
            override val argument0: Int,
            override val argument1: Int,
            override val argument2: Int,
            override val targetName: String,
            override val action: String,
            override val shiftClick: Boolean
    ) : MenuOption

    companion object {

        fun of(opcode: Int, argument0: Int, argument1: Int, argument2: Int, targetName: String, action: String, shiftClick: Boolean): MenuOption {
            val base = MenuOption.Base(opcode, argument0, argument1, argument2, targetName, action, shiftClick)
            return when(opcode) {
                // todo: subclasses
                OBJECT_ACTION_0, OBJECT_ACTION_1, OBJECT_ACTION_2, OBJECT_ACTION_3, OBJECT_ACTION_4 ->
                    MenuOption.OnObjectIndexed(base)
                CANCEL ->
                    MenuOption.Cancel(base)
                WALK_HERE ->
                    MenuOption.WalkHere(base)
                NPC_ACTION_0, NPC_ACTION_1, NPC_ACTION_2, NPC_ACTION_3, NPC_ACTION_4 ->
                    MenuOption.OnNpcIndexed(base)
                PLAYER_ACTION_0, PLAYER_ACTION_1, PLAYER_ACTION_2, PLAYER_ACTION_3, PLAYER_ACTION_4, PLAYER_ACTION_5,
                PLAYER_ACTION_6, PLAYER_ACTION_7 ->
                    MenuOption.OnPlayerIndexed(base)
                PLAYER_ACTION_0_LOW_PRIORITY, PLAYER_ACTION_1_LOW_PRIORITY, PLAYER_ACTION_2_LOW_PRIORITY,
                PLAYER_ACTION_3_LOW_PRIORITY, PLAYER_ACTION_4_LOW_PRIORITY, PLAYER_ACTION_5_LOW_PRIORITY,
                PLAYER_ACTION_6_LOW_PRIORITY, PLAYER_ACTION_7_LOW_PRIORITY ->
                    MenuOption.OnPlayerIndexedLowPriority(base)
                SPELL_ON_NPC, ITEM_ON_NPC, NPC_EXAMINE ->
                    MenuOption.OnNpcSimple(base)
                SPELL_ON_PLAYER, ITEM_ON_PLAYER ->
                    MenuOption.OnPlayerSimple(base)
                SPELL_ON_OBJECT, OBJECT_EXAMINE, ITEM_ON_OBJECT ->
                    MenuOption.OnObjectSimple(base)
                GROUND_ITEM_ACTION_0, GROUND_ITEM_ACTION_1, GROUND_ITEM_ACTION_2, GROUND_ITEM_ACTION_3, GROUND_ITEM_ACTION_4 ->
                    MenuOption.OnGroundItemIndexed(base)
                ITEM_ON_GROUND_ITEM, GROUND_ITEM_EXAMINE, SPELL_ON_GROUND_ITEM ->
                    MenuOption.OnGroundItemSimple(base)
                ITEM_ACTION_0, ITEM_ACTION_1, ITEM_ACTION_2, ITEM_ACTION_3, ITEM_ACTION_4 ->
                    MenuOption.OnItemIndexed(base)
                ITEM_EXAMINE, SPELL_ON_ITEM, ITEM_ON_ITEM, USE_ITEM ->
                    MenuOption.OnItemSimple(base)
                WIDGET_ACTION_0, WIDGET_ACTION_1, BUTTON_SPELL ->
                    MenuOption.OnWidgetSimple(base)
                BUTTON_DIALOG ->
                    MenuOption.ButtonDialog(base)
                else ->
                    base
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

        val id: Int get() = argument0
    }

    interface OnGroundItem: MenuOption {

        val id get() = argument0
    }

    interface OnItem: MenuOption, InWidget {

        val id get() = argument0

        val slot get() = argument1
    }

    interface InWidget: MenuOption {

        val widgetId get() = WidgetId(argument2)

        val widget: Widget get() = checkNotNull(Widgets[widgetId])
    }

    interface OnWidget: MenuOption, InWidget {

        val widgetChildId: Int? get() = argument1.let { if (it == -1) null else it }

        val widgetChild: Widget? get() = widget.let { p -> widgetChildId?.let { c -> (p as Widget.Layer).dynamicChildren[c] } ?: p }
    }

    data class OnWidgetSimple internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, OnWidget {

        override fun toString(): String {
            return "OnWidgetSimple(opcode=$opcode, widgetId=$widgetId, widgetChildId=$widgetChildId, targetName=$targetName, action=$action)"
        }
    }

    data class ButtonDialog internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, InWidget, Indexed {

        override val index get() = argument1

        override fun toString(): String {
            return "ButtonDialog(opcode=$opcode, index=$index, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnItemIndexed internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, Indexed, OnItem {

        override val index get() = opcode - ITEM_ACTION_0

        override fun toString(): String {
            return "OnItemIndexed(opcode=$opcode, index=$index, id=$id, slot=$slot, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnItemSimple internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, OnItem {

        override fun toString(): String {
            return "OnItemSimple(opcode=$opcode, id=$id, slot=$slot, widgetId=$widgetId, targetName=$targetName, action=$action)"
        }
    }

    data class OnGroundItemIndexed internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, Indexed, OnGroundItem, AtLocation {

        override val plane: Int = CLIENT.plane

        override val index: Int get() = opcode - GROUND_ITEM_ACTION_0

        override fun toString(): String {
            return "OnGroundItemIndexed(opcode=$opcode, index=$index, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }

    data class OnGroundItemSimple internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, OnGroundItem, AtLocation {

        override val plane: Int = CLIENT.plane

        override fun toString(): String {
            return "OnGroundItemSimple(opcode=$opcode, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }

    data class Cancel internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption {

        override fun toString(): String {
            return "Cancel(opcode=$opcode, action=$action)"
        }
    }

    data class WalkHere internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, OnScreen {

        override fun toString(): String {
            return "WalkHere(opcode=$opcode, screenLocation=$screenLocation, action=$action)"
        }
    }

    data class OnObjectIndexed internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, AtLocation, Indexed, OnObject {

        override val plane: Int = CLIENT.plane

        override val index: Int get() = opcode - OBJECT_ACTION_0

        override fun toString(): String {
            return "OnObjectIndexed(opcode=$opcode, index=$index, id=$id, targetName=$targetName, action=$action)"
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

    data class OnNpcIndexed internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, Indexed, OnNpc {

        override val index: Int get() = opcode - NPC_ACTION_0

        override fun toString(): String {
            return "OnNpcIndexed(opcode=$opcode, index=$index, npcTargetIndex=$npcTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerIndexed internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, Indexed, OnPlayer {

        override val index: Int get() = opcode - PLAYER_ACTION_0

        override fun toString(): String {
            return "OnPlayerIndexed(opcode=$opcode, index=$index, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerIndexedLowPriority internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, Indexed, OnPlayer {

        override val index: Int get() = opcode - PLAYER_ACTION_0_LOW_PRIORITY

        override fun toString(): String {
            return "OnPlayerIndexedLowPriority(opcode=$opcode, index=$index, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnNpcSimple internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, OnNpc {

        override fun toString(): String {
            return "OnNpcSimple(opcode=$opcode, npcTargetIndex=$npcTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerSimple internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, OnPlayer {

        override fun toString(): String {
            return "OnPlayerSimple(opcode=$opcode, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnObjectSimple internal constructor(private val menuOption: MenuOption) : MenuOption by menuOption, OnObject, AtLocation {

        override val plane: Int = CLIENT.plane

        override fun toString(): String {
            return "OnObjectSimple(opcode=$opcode, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }
}

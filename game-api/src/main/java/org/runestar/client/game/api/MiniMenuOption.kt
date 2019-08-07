package org.runestar.client.game.api

import org.runestar.client.game.api.MiniMenuOpcode.*
import org.runestar.client.game.api.live.Components
import org.runestar.client.game.raw.CLIENT
import java.awt.Point

interface MiniMenuOption {

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
    ) : MiniMenuOption

    companion object {

        fun of(opcode: Int, argument0: Int, argument1: Int, argument2: Int, targetName: String, action: String, shiftClick: Boolean): MiniMenuOption {
            val base = MiniMenuOption.Base(opcode, argument0, argument1, argument2, targetName, action, shiftClick)
            return when(opcode) {
                // todo: subclasses
                OP_LOC1, OP_LOC2, OP_LOC3, OP_LOC4, OP_LOC5 ->
                    MiniMenuOption.OnObjectIndexed(base)
                CANCEL ->
                    MiniMenuOption.Cancel(base)
                WALK ->
                    MiniMenuOption.WalkHere(base)
                OP_NPC1, OP_NPC2, OP_NPC3, OP_NPC4, OP_NPC5 ->
                    MiniMenuOption.OnNpcIndexed(base)
                OP_PLAYER1, OP_PLAYER2, OP_PLAYER3, OP_PLAYER4, OP_PLAYER5, OP_PLAYER6,
                OP_PLAYER7, OP_PLAYER8 ->
                    MiniMenuOption.OnPlayerIndexed(base)
                OP_PLAYER1_LOWPRIORITY, OP_PLAYER2_LOWPRIORITY, OP_PLAYER3_LOWPRIORITY,
                OP_PLAYER4_LOWPRIORITY, OP_PLAYER5_LOWPRIORITY, OP_PLAYER6_LOWPRIORITY,
                OP_PLAYER7_LOWPRIORITY, OP_PLAYER8_LOWPRIORITY ->
                    MiniMenuOption.OnPlayerIndexedLowPriority(base)
                OP_NPCT, OP_NPCU, OP_NPC6 ->
                    MiniMenuOption.OnNpcSimple(base)
                OP_PLAYERT, OP_PLAYERU ->
                    MiniMenuOption.OnPlayerSimple(base)
                OP_LOCT, OP_LOC6, OP_LOCU ->
                    MiniMenuOption.OnObjectSimple(base)
                OP_OBJ1, OP_OBJ2, OP_OBJ3, OP_OBJ4, OP_OBJ5 ->
                    MiniMenuOption.OnGroundItemIndexed(base)
                OP_OBJU, OP_OBJ6, OP_OBJT ->
                    MiniMenuOption.OnGroundItemSimple(base)
                OP_HELD1, OP_HELD2, OP_HELD3, OP_HELD4, OP_HELD5 ->
                    MiniMenuOption.OnItemIndexed(base)
                IF1_BUTTON6, OP_HELDT, OP_HELDU, TGT_HELD ->
                    MiniMenuOption.OnItemSimple(base)
                IF_BUTTONX1, IF_BUTTONX2, TGT_BUTTON ->
                    MiniMenuOption.OnComponentSimple(base)
                PAUSE_BUTTON ->
                    MiniMenuOption.ButtonDialog(base)
                else ->
                    base
            }
        }
    }

    interface AtLocation : MiniMenuOption {

        val plane: Int

        val location: SceneTile get() = SceneTile(argument1, argument2, plane)
    }

    interface Indexed : MiniMenuOption {

        val index: Int
    }

    interface OnScreen : MiniMenuOption {

        val screenLocation get() = Point(argument1, argument2)
    }

    interface OnObject: MiniMenuOption {

        val id: Int get() = argument0
    }

    interface OnGroundItem: MiniMenuOption {

        val id get() = argument0
    }

    interface OnItem: MiniMenuOption, InComponent {

        val id get() = argument0

        val slot get() = argument1
    }

    interface InComponent: MiniMenuOption {

        val componentId get() = ComponentId(argument2)

        val component: Component get() = checkNotNull(Components[componentId])
    }

    interface OnComponent: MiniMenuOption, InComponent {

        val componentChildId: Int? get() = argument1.let { if (it == -1) null else it }

        val componentChild: Component? get() = component.let { p -> componentChildId?.let { c -> p.dynamicChildren[c] } ?: p }
    }

    data class OnComponentSimple internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, OnComponent {

        override fun toString(): String {
            return "OnComponentSimple(opcode=$opcode, componentId=$componentId, componentChildId=$componentChildId, targetName=$targetName, action=$action)"
        }
    }

    data class ButtonDialog internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, InComponent, Indexed {

        override val index get() = argument1

        override fun toString(): String {
            return "ButtonDialog(opcode=$opcode, index=$index, componentId=$componentId, targetName=$targetName, action=$action)"
        }
    }

    data class OnItemIndexed internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, Indexed, OnItem {

        override val index get() = opcode - OP_HELD1

        override fun toString(): String {
            return "OnItemIndexed(opcode=$opcode, index=$index, id=$id, slot=$slot, componentId=$componentId, targetName=$targetName, action=$action)"
        }
    }

    data class OnItemSimple internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, OnItem {

        override fun toString(): String {
            return "OnItemSimple(opcode=$opcode, id=$id, slot=$slot, componentId=$componentId, targetName=$targetName, action=$action)"
        }
    }

    data class OnGroundItemIndexed internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, Indexed, OnGroundItem, AtLocation {

        override val plane: Int = CLIENT.plane

        override val index: Int get() = opcode - OP_OBJ1

        override fun toString(): String {
            return "OnGroundItemIndexed(opcode=$opcode, index=$index, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }

    data class OnGroundItemSimple internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, OnGroundItem, AtLocation {

        override val plane: Int = CLIENT.plane

        override fun toString(): String {
            return "OnGroundItemSimple(opcode=$opcode, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }

    data class Cancel internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption {

        override fun toString(): String {
            return "Cancel(opcode=$opcode, action=$action)"
        }
    }

    data class WalkHere internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, OnScreen {

        override fun toString(): String {
            return "WalkHere(opcode=$opcode, screenLocation=$screenLocation, action=$action)"
        }
    }

    data class OnObjectIndexed internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, AtLocation, Indexed, OnObject {

        override val plane: Int = CLIENT.plane

        override val index: Int get() = opcode - OP_LOC1

        override fun toString(): String {
            return "OnObjectIndexed(opcode=$opcode, index=$index, id=$id, targetName=$targetName, action=$action)"
        }
    }

    interface OnNpc : MiniMenuOption, ActorTargeting {

        override val playerTargetIndex: Int get() = -1

        override val npcTargetIndex: Int get() = argument0

        override val target: Npc? get() = super.target as Npc?
    }

    interface OnPlayer : MiniMenuOption, ActorTargeting {

        override val playerTargetIndex: Int get() = argument0

        override val npcTargetIndex: Int get() = -1

        override val target: Player? get() = super.target as Player?
    }

    data class OnNpcIndexed internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, Indexed, OnNpc {

        override val index: Int get() = opcode - OP_NPC1

        override fun toString(): String {
            return "OnNpcIndexed(opcode=$opcode, index=$index, npcTargetIndex=$npcTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerIndexed internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, Indexed, OnPlayer {

        override val index: Int get() = opcode - OP_PLAYER1

        override fun toString(): String {
            return "OnPlayerIndexed(opcode=$opcode, index=$index, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerIndexedLowPriority internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, Indexed, OnPlayer {

        override val index: Int get() = opcode - OP_PLAYER1_LOWPRIORITY

        override fun toString(): String {
            return "OnPlayerIndexedLowPriority(opcode=$opcode, index=$index, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnNpcSimple internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, OnNpc {

        override fun toString(): String {
            return "OnNpcSimple(opcode=$opcode, npcTargetIndex=$npcTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnPlayerSimple internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, OnPlayer {

        override fun toString(): String {
            return "OnPlayerSimple(opcode=$opcode, playerTargetIndex=$playerTargetIndex, targetName=$targetName, action=$action)"
        }
    }

    data class OnObjectSimple internal constructor(private val menuOption: MiniMenuOption) : MiniMenuOption by menuOption, OnObject, AtLocation {

        override val plane: Int = CLIENT.plane

        override fun toString(): String {
            return "OnObjectSimple(opcode=$opcode, id=$id, location=$location, targetName=$targetName, action=$action)"
        }
    }
}

package org.runestar.client.game.api

sealed class HintArrow {

    abstract val position: Position

    abstract class OnActor : HintArrow() {

        abstract val target: Actor

        override val position: Position get() = target.let { it.modelPosition.copy(height = it.defaultHeight + 15) }
    }

    data class OnPlayer(override val target: Player) : OnActor()

    data class OnNpc(override val target: Npc) : OnActor()

    data class Static(override val position: Position) : HintArrow()
}
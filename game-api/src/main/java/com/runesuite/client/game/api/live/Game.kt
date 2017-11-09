package com.runesuite.client.game.api.live

import com.hunterwb.kxtra.swing.component.windowAncestor
import com.runesuite.client.game.api.GameState
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.game.raw.access.XClient
import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import java.awt.Component
import java.awt.Container

object Game {

    val state get() = checkNotNull(GameState.LOOKUP[accessor.gameState]) { accessor.gameState }

    val stateChanges: Observable<GameState> = XClient.updateGameState.exit.map {
        checkNotNull(GameState.LOOKUP[it.arguments[0]]) { it.arguments[0] }
    }

    val cycle get() = accessor.cycle

    val plane get() = accessor.plane

    val runEnergy get() = accessor.runEnergy

    val weight get() = accessor.weight

    val windowMode get() = checkNotNull(WindowMode.LOOKUP[accessor.clientPreferences.windowMode]) { accessor.clientPreferences.windowMode }

    /**
     * @see[java.awt.event.WindowListener][java.awt.event.WindowStateListener][java.awt.event.WindowFocusListener]
     */
    val windowEvents = SwingObservable.window(
            checkNotNull((accessor as Component).windowAncestor()) { "Client has no window" }
    )

    /**
     * @see[java.awt.event.ContainerListener]
     */
    val containerEvents = SwingObservable.container(accessor as Container)

    enum class WindowMode(val id: Int) {

        FIXED(1),
        RESIZABLE(2);

        companion object {
            @JvmField
            val LOOKUP = values().associateBy { it.id }
        }
    }
}
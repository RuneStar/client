package com.runesuite.client.game.api.live

import com.hunterwb.kxtra.swing.component.windowAncestor
import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.game.raw.access.XClient
import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import java.applet.Applet

object Game {

    val state get() = checkNotNull(State.LOOKUP[accessor.gameState]) { accessor.gameState }

    val stateChanges: Observable<State> = XClient.updateGameState.exit.map {
        checkNotNull(State.LOOKUP[it.arguments[0]]) { it.arguments[0] }
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
            checkNotNull((accessor as Applet).windowAncestor()) { "Client has no window" }
    )

    /**
     * @see[java.awt.event.ContainerListener]
     */
    val containerEvents = SwingObservable.container(Client.accessor as Applet)

    enum class State(val id: Int) {

        NONE(0),
        STARTING(5),
        TITLE(10),
        LOGGING_IN(20),
        LOADING(25),
        LOGGED_IN(30),
        CONNECTION_LOST(40),
        CHANGING_WORLD(45);

        companion object {
            val LOOKUP = values().associateBy { it.id }
        }
    }

    enum class WindowMode(val id: Int) {

        FIXED(1),
        RESIZABLE(2);

        companion object {
            val LOOKUP = values().associateBy { it.id }
        }
    }
}
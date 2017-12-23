package org.runestar.client.game.api.live

import org.kxtra.swing.component.windowAncestor
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.Varps
import org.runestar.client.game.api.WindowMode
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.game.raw.access.XClient
import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import java.awt.Component
import java.awt.Container

object Game {

    val state get() = GameState.LOOKUP.getValue(accessor.gameState)

    val stateChanges: Observable<GameState> = XClient.updateGameState.exit.map {
        checkNotNull(GameState.LOOKUP[it.arguments[0]]) { it.arguments[0] }
    }

    val cycle get() = accessor.cycle

    val plane get() = accessor.plane

    val runEnergy get() = accessor.runEnergy

    val weight get() = accessor.weight

    val windowMode get() = WindowMode.LOOKUP.getValue(accessor.clientPreferences.windowMode)

    /**
     * @see[java.awt.event.WindowListener]
     * @see[java.awt.event.WindowStateListener]
     * @see[java.awt.event.WindowFocusListener]
     */
    val windowEvents = SwingObservable.window(
            checkNotNull((accessor as Component).windowAncestor()) { "Client has no window" }
    )

    /**
     * @see[java.awt.event.ContainerListener]
     */
    val containerEvents = SwingObservable.container(accessor as Container)

    val varps = Varps(Client.accessor.varps)
}
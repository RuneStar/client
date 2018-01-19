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
import org.runestar.client.game.raw.access.XPacketBuffer
import java.awt.Component
import java.awt.Container

object Game {

    val state get() = GameState.of(accessor.gameState)

    val stateChanges: Observable<GameState> = XClient.updateGameState.exit.map {
        checkNotNull(GameState.of(it.arguments[0] as Int)) { it.arguments[0] }
    }

    val ticks: Observable<Unit> = XPacketBuffer.readSmartByteShortIsaac.exit
            .filter { it.returned == 38 }
            .map { Unit }

    val cycle get() = accessor.cycle

    val plane get() = accessor.plane

    val runEnergy get() = accessor.runEnergy

    val weight get() = accessor.weight

    val windowMode get() = WindowMode.of(accessor.clientPreferences.windowMode)

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
package com.runesuite.client.game.live

import com.runesuite.client.base.Client
import com.runesuite.client.base.access.XGameShell
import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import java.awt.event.KeyEvent

object Keyboard {

    /**
     * @see[java.awt.event.KeyListener]
     */
    val events: Observable<KeyEvent> = XGameShell.replaceCanvas.EXIT.map { Client.accessor.canvas }.startWith(Client.accessor.canvas)
            .flatMap { SwingObservable.keyboard(it) }
}
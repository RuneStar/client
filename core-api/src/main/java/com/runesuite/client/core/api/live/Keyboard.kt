package com.runesuite.client.core.api.live

import com.runesuite.client.core.raw.Client
import com.runesuite.client.core.raw.access.XGameShell
import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import java.awt.event.KeyEvent

object Keyboard {

    /**
     * @see[java.awt.event.KeyListener]
     */
    val events: Observable<KeyEvent> = XGameShell.replaceCanvas.exit.map { Client.accessor.canvas }.startWith(Client.accessor.canvas)
            .flatMap { SwingObservable.keyboard(it) }
}
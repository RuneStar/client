package org.runestar.client.game.api.live

import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import org.runestar.client.game.raw.Client
import java.awt.event.KeyEvent

object Keyboard {

    /**
     * @see[java.awt.event.KeyListener]
     */
    val events: Observable<KeyEvent> = LiveCanvas.canvasReplacements.flatMap { SwingObservable.keyboard(it) }

    /**
     * @see[KeyEvent.keyCode]
     */
    fun isKeySupported(keyCode: Int): Boolean {
        return keyCode in Client.accessor.keyHandler_keyCodes.indices && Client.accessor.keyHandler_keyCodes[keyCode] >= 0
    }

    /**
     * @see[KeyEvent.keyCode]
     */
    fun isKeyPressed(keyCode: Int): Boolean {
        require(isKeySupported(keyCode)) { "Unsupported KeyCode: $keyCode" }
        return Client.accessor.keyHandler_pressedKeys[Client.accessor.keyHandler_keyCodes[keyCode]]
    }
}
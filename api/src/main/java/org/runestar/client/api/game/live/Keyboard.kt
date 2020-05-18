package org.runestar.client.api.game.live

import hu.akarnokd.rxjava3.swing.SwingObservable
import io.reactivex.rxjava3.core.Observable
import org.kxtra.swing.input.keyStroke
import org.runestar.client.raw.CLIENT
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

object Keyboard {

    /**
     * @see[java.awt.event.KeyListener]
     */
    val events: Observable<KeyEvent> get() = Canvas.canvasReplacements.flatMap { SwingObservable.keyboard(it) }

    val strokes: Observable<KeyStroke> get() = events.map { it.keyStroke }

    /**
     * @see[KeyEvent.keyCode]
     */
    fun isKeySupported(keyCode: Int): Boolean {
        return keyCode in CLIENT.keyHandler_keyCodes.indices && CLIENT.keyHandler_keyCodes[keyCode] >= 0
    }

    /**
     * @see[KeyEvent.keyCode]
     */
    fun isKeyPressed(keyCode: Int): Boolean {
        require(isKeySupported(keyCode)) { "Unsupported KeyCode: $keyCode" }
        return CLIENT.keyHandler_pressedKeys[CLIENT.keyHandler_keyCodes[keyCode]]
    }
}
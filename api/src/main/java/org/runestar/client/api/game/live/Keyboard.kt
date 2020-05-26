package org.runestar.client.api.game.live

import io.reactivex.rxjava3.core.Observable
import org.kxtra.swing.input.keyStroke
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XKeyHandler
import org.runestar.client.raw.base.MethodEvent
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

object Keyboard {

    val methods: Observable<MethodEvent<XKeyHandler, Void>> = Observable.mergeArray(
            XKeyHandler.keyPressed.enter,
            XKeyHandler.keyReleased.enter,
            XKeyHandler.keyTyped.enter
    )

    val events: Observable<KeyEvent> = methods.map { it.arguments[0] as KeyEvent }

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
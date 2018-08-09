package org.runestar.client.api.forms

import java.awt.event.KeyEvent
import java.util.function.Supplier
import javax.swing.KeyStroke

data class KeyStrokeForm(val description: String) : Supplier<KeyStroke> {

    @Transient
    private val value: KeyStroke = KeyStroke.getKeyStroke(description)

    override fun get(): KeyStroke = value

    fun matches(keyEvent: KeyEvent): Boolean {
        return KeyStroke.getKeyStrokeForEvent(keyEvent) == value
    }
}
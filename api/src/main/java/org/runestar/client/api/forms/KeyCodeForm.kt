package org.runestar.client.api.forms

import java.util.function.Supplier
import javax.swing.KeyStroke

data class KeyCodeForm(
        val name: String
) : Supplier<Int> {

    @Transient
    private val value: Int = KeyStroke.getKeyStroke(name).keyCode

    override fun get(): Int = value
}
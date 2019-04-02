package org.runestar.client.api.forms

import javax.swing.KeyStroke

data class KeyCodeForm(
        val name: String
) {

    @Transient val value: Int = KeyStroke.getKeyStroke(name).keyCode
}
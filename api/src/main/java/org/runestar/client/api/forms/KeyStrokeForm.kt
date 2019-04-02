package org.runestar.client.api.forms

import javax.swing.KeyStroke

data class KeyStrokeForm(
        val description: String
) {

    @Transient val value: KeyStroke = KeyStroke.getKeyStroke(description)
}
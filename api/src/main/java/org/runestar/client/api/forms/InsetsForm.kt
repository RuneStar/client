package org.runestar.client.api.forms

import java.awt.Insets

data class InsetsForm(
        val top: Int,
        val left: Int,
        val bottom: Int,
        val right: Int
) {
    @Transient val value: Insets = Insets(top, left, bottom, right)
}
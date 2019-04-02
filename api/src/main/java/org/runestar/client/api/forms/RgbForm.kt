package org.runestar.client.api.forms

import java.awt.Color

data class RgbForm(
        val red: Int,
        val green: Int,
        val blue: Int
) {

    constructor(color: Color) : this(color.red, color.green, color.blue)

    @Transient val value = Color(red, green, blue)
}
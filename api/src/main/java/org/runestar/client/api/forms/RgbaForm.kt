package org.runestar.client.api.forms

import java.awt.Color

data class RgbaForm(
        val red: Int,
        val green: Int,
        val blue: Int,
        val alpha: Int = 255
) {

    constructor(color: Color) : this(color.red, color.green, color.blue, color.alpha)

    @Transient val value = Color(red, green, blue, alpha)
}
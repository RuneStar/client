package org.runestar.client.api.forms

import java.awt.Color
import java.util.function.Supplier

data class RgbaForm(
        val red: Int,
        val green: Int,
        val blue: Int,
        val alpha: Int = 255
) : Supplier<Color> {

    constructor(color: Color) : this(color.red, color.green, color.blue, color.alpha)

    @Transient
    private val value = Color(red, green, blue, alpha)

    override fun get() = value
}
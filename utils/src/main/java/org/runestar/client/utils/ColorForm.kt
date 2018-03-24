package org.runestar.client.utils

import java.awt.Color
import java.util.function.Supplier

data class ColorForm(
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
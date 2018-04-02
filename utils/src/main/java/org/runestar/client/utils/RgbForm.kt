package org.runestar.client.utils

import java.awt.Color
import java.util.function.Supplier

data class RgbForm(
        val red: Int,
        val green: Int,
        val blue: Int
) : Supplier<Color> {

    constructor(color: Color) : this(color.red, color.green, color.blue)

    @Transient
    private val value = Color(red, green, blue)

    override fun get() = value
}
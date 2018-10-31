package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XVarcs

inline class Varcs(val accessor: XVarcs) {

    val ints: IntArray get() = accessor.ints

    val strings: Array<String?> get() = accessor.strings

    fun getInt(index: Int): Int = accessor.getInt(index)

    fun setInt(index: Int, value: Int): Unit = accessor.setInt(index, value)

    fun getString(index: Int): String? = accessor.getString(index)

    fun setString(index: Int, value: String?): Unit = accessor.setString(index, value)
}
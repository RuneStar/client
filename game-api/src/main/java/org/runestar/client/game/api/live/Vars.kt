package org.runestar.client.game.api.live

import org.runestar.client.game.raw.CLIENT

object Vars {

    fun getVarbit(id: Int): Int = CLIENT.getVarbit(id)

    fun getVarp(id: Int): Int = CLIENT.varps_main[id]

    fun getVarcInt(id: Int): Int = CLIENT.varcs.getInt(id)

    fun setVarcInt(id: Int, value: Int) = CLIENT.varcs.setInt(id, value)

    fun getVarcString(id: Int): String? = CLIENT.varcs.getString(id)

    fun setVarcString(id: Int, value: String?) = CLIENT.varcs.setString(id, value)
}
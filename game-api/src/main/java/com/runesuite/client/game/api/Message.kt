package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XMessage

class Message(override val accessor: XMessage) : Wrapper() {

    var sender: String
        get() = accessor.sender
        set(value) { accessor.sender = value }

    var prefix: String?
        get() = accessor.prefix
        set(value) { accessor.prefix = value }

    var text: String
        get() = accessor.text
        set(value) { accessor.text = value }

    val type get() = accessor.type

    override fun toString(): String {
        return "Message(type=$type, prefix='$prefix', sender='$sender', text='$text')"
    }

    private fun String.unescape() = replace("<lt>", "<").replace("<gt>", ">")

    private fun String.escape() = replace("<", "<lt>").replace(">", "<gt>")

    enum class Type(val id: Int) {

        SYSTEM(0),
        PUBLIC_CHAT(2),
        CLAN_CHAT(9),
        SYSTEM_CLAN_CHAT(11),
        ITEM_EXAMINE(27),
        NPC_EXAMINE(28),
        OBJECT_EXAMINE(29),
        SYSTEM_FRIENDS(30),
        SYSTEM_IGNORE(31),
        AUTO_CHAT(90),
        // todo
        SYSTEM_FILTERED(105);

        companion object {
            val LOOKUP = values().associateBy { it.id }
        }
    }
}
package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XMessage

class Message(val accessor: XMessage) {

    var sender: String
        get() = accessor.sender
        set(value) { accessor.sender = value }

    val senderUsername: Username? get() = accessor.senderUsername?.let { Username(it) }

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
}
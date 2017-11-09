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
}
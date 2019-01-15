package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XMessage
import java.lang.StringBuilder

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

    fun join(): String = joinTo(StringBuilder()).toString()

    fun joinTo(sb: StringBuilder): StringBuilder {
        if (prefix != null) {
            sb.append('[')
            sb.append(prefix)
            sb.append(']')
            sb.append(' ')
        }
        if (sender.isNotEmpty()) {
            sb.append(unescapeSpaces(sender))
            sb.append(':')
            sb.append(' ')
        }
        sb.append(unescapeAngleBrackets(text))
        return sb
    }

    override fun toString(): String {
        return "Message(type=$type, prefix='$prefix', sender='$sender', text='$text')"
    }
}
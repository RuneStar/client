package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XChatChannel

class ChatChannel(val accessor: XChatChannel) : AbstractList<Message>(), RandomAccess {

    override val size: Int get() = accessor.count

    override fun get(index: Int): Message {
        return Message(accessor.messages[index])
    }
}
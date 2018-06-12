package org.runestar.client.game.api.live

import io.reactivex.Observable
import org.runestar.client.game.api.Message
import org.runestar.client.game.api.PublicChatMode
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.game.raw.access.XChatChannel

object Chat {

    val messages: Observable<Message> = XChatChannel.addMessage.exit.map { Message(it.returned) }

    val publicMode get() = PublicChatMode.of(accessor.publicChatMode)
}
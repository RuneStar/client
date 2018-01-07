package org.runestar.client.game.api.live

import org.runestar.client.game.api.Message
import org.runestar.client.game.api.PublicChatMode
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.game.raw.access.XChatBox
import io.reactivex.Observable

object Chat {

    val messages: Observable<Message> = XChatBox.addMessage.exit.map { Message(it.returned) }

    val publicMode get() = PublicChatMode.of(accessor.publicChatMode)
}
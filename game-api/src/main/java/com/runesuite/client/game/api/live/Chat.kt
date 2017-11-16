package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Message
import com.runesuite.client.game.api.PublicChatMode
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.game.raw.access.XChatBox
import io.reactivex.Observable

object Chat {

    val messages: Observable<Message> = XChatBox.addMessage.exit.map { Message(it.returned) }

    val publicMode get() = checkNotNull(PublicChatMode.LOOKUP[accessor.publicChatMode]) { accessor.publicChatMode }
}
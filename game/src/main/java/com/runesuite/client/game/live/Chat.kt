package com.runesuite.client.game.live

import com.runesuite.client.base.access.XChatBox
import com.runesuite.client.base.access.XMessage
import com.runesuite.client.game.Message
import io.reactivex.Observable

object Chat {

    val messages: Observable<Message> = XChatBox.addMessage.exit.map { Message(it.returned as XMessage) }
}
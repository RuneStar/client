package com.runesuite.client.api.live

import com.runesuite.client.api.Message
import com.runesuite.client.raw.access.XChatBox
import com.runesuite.client.raw.access.XMessage
import io.reactivex.Observable

object Chat {

    val messages: Observable<Message> = XChatBox.addMessage.exit.map { Message(it.returned as XMessage) }
}
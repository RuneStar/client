package com.runesuite.client.core.api.live

import com.runesuite.client.core.api.Message
import com.runesuite.client.core.raw.access.XChatBox
import com.runesuite.client.core.raw.access.XMessage
import io.reactivex.Observable

object Chat {

    val messages: Observable<Message> = XChatBox.addMessage.exit.map { Message(it.returned as XMessage) }
}
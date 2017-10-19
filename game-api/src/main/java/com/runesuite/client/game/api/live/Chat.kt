package com.runesuite.client.game.api.live

import com.runesuite.client.game.raw.access.XChatBox
import com.runesuite.client.game.raw.access.XMessage
import io.reactivex.Observable

object Chat {

    val messages: Observable<com.runesuite.client.game.api.Message> = XChatBox.addMessage.exit.map { com.runesuite.client.game.api.Message(it.returned) }
}
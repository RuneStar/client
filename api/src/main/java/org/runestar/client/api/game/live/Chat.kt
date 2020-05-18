package org.runestar.client.api.game.live

import com.google.common.collect.Iterators
import io.reactivex.rxjava3.core.Observable
import org.runestar.client.api.game.Message
import org.runestar.client.api.game.VarcStringId
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XChatChannel
import org.runestar.client.raw.access.XMessage

object Chat : Iterable<Message> {

    override fun iterator(): Iterator<Message> {
        return Iterators.transform(CLIENT.messages_queue.iterator()) { Message(it as XMessage) }
    }

    val messageAdditions: Observable<Message> = XChatChannel.addMessage.exit.map { Message(it.returned) }

    val publicMode: Int get() = CLIENT.publicChatMode

    var typedText: String?
        get() = Vars.getVarcString(VarcStringId.TYPED_TEXT)
        set(value) { Vars.setVarcString(VarcStringId.TYPED_TEXT, value) }
}
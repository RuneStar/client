package org.runestar.client.game.api.live

import com.google.common.collect.Iterators
import io.reactivex.Observable
import org.runestar.client.game.api.Message
import org.runestar.client.game.api.PublicChatMode
import org.runestar.client.game.api.VarcStringId
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XChatChannel
import org.runestar.client.game.raw.access.XMessage

object Chat : Iterable<Message> {

    override fun iterator(): Iterator<Message> {
        return Iterators.transform(CLIENT.messages_queue.iterator()) { Message(it as XMessage) }
    }

    val messageAdditions: Observable<Message> = XChatChannel.addMessage.exit.map { Message(it.returned) }

    val publicMode get() = PublicChatMode.of(CLIENT.publicChatMode)

    var typedText: String?
        get() = Game.varcs.getString(VarcStringId.TYPED_TEXT)
        set(value) { Game.varcs.setString(VarcStringId.TYPED_TEXT, value) }
}
package org.runestar.client.game.api.live

import com.google.common.collect.Iterators
import io.reactivex.Observable
import org.runestar.client.game.api.Message
import org.runestar.client.game.api.PublicChatMode
import org.runestar.client.game.api.VarcStringId
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.game.raw.access.XChatChannel
import org.runestar.client.game.raw.access.XMessage

object Chat : Iterable<Message> {

    override fun iterator(): Iterator<Message> {
        return Iterators.transform(Client.accessor.messages_queue.iterator()) { Message(it as XMessage) }
    }

    val messageAdditions: Observable<Message> = XChatChannel.addMessage.exit.map { Message(it.returned) }

    val publicMode get() = PublicChatMode.of(accessor.publicChatMode)

    var typedText: String?
        get() = Game.varcs.getString(VarcStringId.TYPED_TEXT)
        set(value) { Game.varcs.setString(VarcStringId.TYPED_TEXT, value) }
}
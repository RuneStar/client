package org.runestar.client.game.api.live

import org.runestar.client.game.api.ClanMate
import org.runestar.client.game.raw.Client.accessor

object ClanChat : AbstractList<ClanMate>(), RandomAccess {

    val capacity = 100

    val owner: String? get() = accessor.clanChatOwner

    val name: String? get() = accessor.clanChatName

    override val size get() = accessor.clanChatCount

    override fun get(index: Int) = ClanMate(accessor.clanChat[index])
}
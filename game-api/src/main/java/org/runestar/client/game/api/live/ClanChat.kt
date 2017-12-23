package org.runestar.client.game.api.live

import org.runestar.client.game.api.ClanMate
import org.runestar.client.game.raw.Client.accessor

object ClanChat {

    val CAPACITY = 100

    val owner: String? get() = accessor.clanChatOwner

    fun get(): List<ClanMate?> = accessor.clanChat?.map { it?.let { ClanMate(it) } } ?: emptyList()

    val all: List<ClanMate> get() = accessor.clanChat?.mapNotNull { it?.let { ClanMate(it) } } ?: emptyList()
}
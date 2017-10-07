package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.ClanMate
import com.runesuite.client.game.raw.Client.accessor

object ClanChat {

    val SIZE = 100

    val owner: String? get() = accessor.clanChatOwner

    fun get(): List<com.runesuite.client.game.api.ClanMate?> = accessor.clanChat?.map { it?.let { com.runesuite.client.game.api.ClanMate(it) } } ?: emptyList()

    val all: List<com.runesuite.client.game.api.ClanMate> get() = accessor.clanChat?.mapNotNull { it?.let { com.runesuite.client.game.api.ClanMate(it) } } ?: emptyList()
}
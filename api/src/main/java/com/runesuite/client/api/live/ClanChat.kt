package com.runesuite.client.api.live

import com.runesuite.client.api.ClanMate
import com.runesuite.client.raw.Client.accessor

object ClanChat {

    val SIZE = 100

    val owner: String? get() = accessor.clanChatOwner

    fun get(): List<ClanMate?> = accessor.clanChat?.map { it?.let { ClanMate(it) } } ?: emptyList()

    val all: List<ClanMate> get() = accessor.clanChat?.mapNotNull { it?.let { ClanMate(it) } } ?: emptyList()
}
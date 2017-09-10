package com.runesuite.client.core.api.live

import com.runesuite.client.core.raw.Client.accessor

object ClanChat {

    val SIZE = 100

    val owner: String? get() = accessor.clanChatOwner

    fun get(): List<com.runesuite.client.core.api.ClanMate?> = accessor.clanChat?.map { it?.let { com.runesuite.client.core.api.ClanMate(it) } } ?: emptyList()

    val all: List<com.runesuite.client.core.api.ClanMate> get() = accessor.clanChat?.mapNotNull { it?.let { com.runesuite.client.core.api.ClanMate(it) } } ?: emptyList()
}
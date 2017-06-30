package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.ClanMate

object ClanChat {

    val SIZE = 100

    fun get(): List<ClanMate?> = accessor.clanChat?.map { it?.let { ClanMate(it) } } ?: emptyList()

    val all: List<ClanMate> get() = accessor.clanChat?.mapNotNull { it?.let { ClanMate(it) } } ?: emptyList()
}
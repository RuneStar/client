package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.ClanMate

object ClanChat {

    val SIZE = 100

    fun get(): List<ClanMate?> = accessor.clanChat?.copyOf()?.map { it?.let { ClanMate(it) } } ?: emptyList()

    val all: Sequence<ClanMate> get() = accessor.clanChat?.copyOf()?.asSequence()?.filterNotNull()?.map { ClanMate(it) } ?: emptySequence()
}
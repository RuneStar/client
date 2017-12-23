package org.runestar.client.game.api.live

import org.runestar.client.game.api.Ignored
import org.runestar.client.game.raw.Client.accessor

object IgnoreList {

    val CAPACITY = accessor.ignoreList.size

    fun get(): List<Ignored?> = accessor.ignoreList.map { it?.let { Ignored(it) } }

    val all: List<Ignored> get() = accessor.ignoreList.mapNotNull { it?.let { Ignored(it) } }
}
package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Ignored
import com.runesuite.client.game.raw.Client.accessor

object IgnoreList {

    val CAPACITY = accessor.ignoreList.size

    fun get(): List<Ignored?> = accessor.ignoreList.map { it?.let { Ignored(it) } }

    val all: List<Ignored> get() = accessor.ignoreList.mapNotNull { it?.let { Ignored(it) } }
}
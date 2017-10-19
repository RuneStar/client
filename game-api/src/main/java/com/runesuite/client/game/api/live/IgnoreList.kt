package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Ignored
import com.runesuite.client.game.raw.Client.accessor

object IgnoreList {

    val CAPACITY = accessor.ignoreList.size

    fun get(): List<com.runesuite.client.game.api.Ignored?> = accessor.ignoreList.map { it?.let { com.runesuite.client.game.api.Ignored(it) } }

    val all: List<com.runesuite.client.game.api.Ignored> get() = accessor.ignoreList.mapNotNull { it?.let { com.runesuite.client.game.api.Ignored(it) } }
}
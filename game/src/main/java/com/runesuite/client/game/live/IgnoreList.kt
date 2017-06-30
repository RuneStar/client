package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.Ignored

object IgnoreList {

    val SIZE = accessor.ignoreList.size

    fun get(): List<Ignored?> = accessor.ignoreList.map { it?.let { Ignored(it) } }

    val all: List<Ignored> get() = accessor.ignoreList.mapNotNull { it?.let { Ignored(it) } }
}
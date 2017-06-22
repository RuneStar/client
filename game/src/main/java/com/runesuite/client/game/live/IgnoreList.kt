package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.Ignored

object IgnoreList {

    val SIZE = accessor.ignoreList.size

    fun get(): List<Ignored?> = accessor.ignoreList.copyOf().map { it?.let { Ignored(it) } }

    val all: Sequence<Ignored> get() = accessor.ignoreList.copyOf().asSequence().filterNotNull().map { Ignored(it) }
}
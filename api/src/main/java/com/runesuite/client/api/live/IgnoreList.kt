package com.runesuite.client.api.live

import com.runesuite.client.api.Ignored
import com.runesuite.client.raw.Client.accessor

object IgnoreList {

    val SIZE = accessor.ignoreList.size

    fun get(): List<Ignored?> = accessor.ignoreList.map { it?.let { Ignored(it) } }

    val all: List<Ignored> get() = accessor.ignoreList.mapNotNull { it?.let { Ignored(it) } }
}
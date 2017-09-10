package com.runesuite.client.core.api.live

import com.runesuite.client.core.raw.Client.accessor

object IgnoreList {

    val SIZE = accessor.ignoreList.size

    fun get(): List<com.runesuite.client.core.api.Ignored?> = accessor.ignoreList.map { it?.let { com.runesuite.client.core.api.Ignored(it) } }

    val all: List<com.runesuite.client.core.api.Ignored> get() = accessor.ignoreList.mapNotNull { it?.let { com.runesuite.client.core.api.Ignored(it) } }
}
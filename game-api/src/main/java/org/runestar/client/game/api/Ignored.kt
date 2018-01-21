package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XIgnored

class Ignored(val accessor: XIgnored) {

    val name: String get() = accessor.name

    val previousName: String? get() = accessor.previousName.takeUnless { it.isNullOrEmpty() }

    override fun toString(): String {
        return "Ignored(name=$name, previousName=$previousName)"
    }
}
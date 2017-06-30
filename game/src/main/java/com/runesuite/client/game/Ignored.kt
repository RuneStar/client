package com.runesuite.client.game

import com.runesuite.client.base.Wrapper
import com.runesuite.client.base.access.XIgnored

class Ignored(override val accessor: XIgnored) : Wrapper() {

    val name: String get() = accessor.name

    val previousName: String? get() = accessor.previousName.takeUnless { it.isNullOrEmpty() }

    override fun toString(): String {
        return "Ignored(name=$name, previousName=$previousName)"
    }
}
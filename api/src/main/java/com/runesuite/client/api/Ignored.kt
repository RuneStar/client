package com.runesuite.client.api

import com.runesuite.client.raw.Wrapper
import com.runesuite.client.raw.access.XIgnored

class Ignored(override val accessor: XIgnored) : Wrapper() {

    val name: String get() = accessor.name

    val previousName: String? get() = accessor.previousName.takeUnless { it.isNullOrEmpty() }

    override fun toString(): String {
        return "Ignored(name=$name, previousName=$previousName)"
    }
}
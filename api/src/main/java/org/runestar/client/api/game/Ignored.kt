package org.runestar.client.api.game

import org.runestar.client.raw.access.XIgnored

class Ignored(override val accessor: XIgnored) : User(accessor) {

    override fun toString(): String {
        return "Ignored(username=$username, previousUsername=$previousUsername)"
    }
}
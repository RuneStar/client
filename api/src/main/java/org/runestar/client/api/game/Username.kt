package org.runestar.client.api.game

import org.runestar.client.raw.access.XUsername

inline class Username(val accessor: XUsername) : Comparable<Username> {

    val name: String get() = accessor.name0

    val cleanName: String? get() = accessor.cleanName

    override fun compareTo(other: Username) = accessor.compareTo0(other.accessor)

    override fun toString(): String {
        return "Username(name=$name, cleanName=$cleanName)"
    }
}
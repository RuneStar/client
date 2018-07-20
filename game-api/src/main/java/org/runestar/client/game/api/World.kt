@file:JvmName("World")

package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XWorld

class World(val accessor: XWorld) {

    val id: Int get() = accessor.id

    val properties: Int get() = accessor.properties

    val host: String get() = accessor.host

    val activity: String get() = accessor.activity

    val location: Int get() = accessor.location

    val population: Int get() = accessor.population

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean = other is World && id == other.id

    override fun toString(): String = "World($id)"
}
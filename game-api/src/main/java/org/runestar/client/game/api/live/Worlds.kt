package org.runestar.client.game.api.live

import com.google.common.collect.Iterators
import io.reactivex.Observable
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.World
import org.runestar.client.game.api.WorldProperty
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.web.hiscore.HiscoreEndpoint

object Worlds : AbstractCollection<World>() {

    override fun iterator(): Iterator<World> {
        return Iterators.transform(CLIENT.worlds.iterator()) { World(checkNotNull(it)) }
    }

    override val size get() = CLIENT.worldsCount

    val local get() = get(CLIENT.worldId)

    operator fun get(id: Int): World = World(CLIENT.worlds.first { it.id == id })

    val enter: Observable<World> = Game.stateChanges
            .filter { it == GameState.CONNECTION_LOST || it == GameState.CHANGING_WORLD || it == GameState.LOGGING_IN }
            .delay { Game.stateChanges.filter { it == GameState.LOGGED_IN } }
            .map { local }

    fun hiscoreEndpoint(properties: Int): HiscoreEndpoint = when {
        (properties and WorldProperty.DEADMAN_TOURNAMENT) != 0 -> HiscoreEndpoint.DEADMAN_TOURNAMENT
        (properties and WorldProperty.SEASONAL) != 0 -> HiscoreEndpoint.SEASONAL_DEADMAN
        (properties and WorldProperty.DEADMAN) != 0 -> HiscoreEndpoint.DEADMAN
        else -> HiscoreEndpoint.NORMAL
    }

    val properties: Int get() = CLIENT.worldProperties

    val hiscoreEndpoint: HiscoreEndpoint get() = hiscoreEndpoint(properties)
}
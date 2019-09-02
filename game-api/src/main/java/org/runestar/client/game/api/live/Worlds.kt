package org.runestar.client.game.api.live

import com.google.common.collect.Iterators
import io.reactivex.Observable
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.World
import org.runestar.client.game.api.WorldProperty
import org.runestar.client.game.api.utils.BitVec
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

    fun hiscoreEndpoint(properties: BitVec): HiscoreEndpoint = when {
        properties[WorldProperty.DEADMAN_TOURNAMENT] -> HiscoreEndpoint.DEADMAN_TOURNAMENT
        properties[WorldProperty.SEASONAL] -> HiscoreEndpoint.SEASONAL_DEADMAN
        properties[WorldProperty.DEADMAN] -> HiscoreEndpoint.DEADMAN
        else -> HiscoreEndpoint.NORMAL
    }

    val properties: BitVec get() = BitVec(CLIENT.worldProperties)

    val hiscoreEndpoint: HiscoreEndpoint get() = hiscoreEndpoint(properties)
}
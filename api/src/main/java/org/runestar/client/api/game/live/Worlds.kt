package org.runestar.client.api.game.live

import com.google.common.collect.Iterators
import io.reactivex.rxjava3.core.Observable
import org.runestar.client.api.game.GameState
import org.runestar.client.api.game.World
import org.runestar.client.api.game.WorldProperty
import org.runestar.client.api.util.BitVec
import org.runestar.client.raw.CLIENT
import org.runestar.client.api.web.hiscore.HiscoreEndpoint

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
        properties[WorldProperty.TOURNAMENT] -> HiscoreEndpoint.TOURNAMENT
        properties[WorldProperty.SEASONAL] -> HiscoreEndpoint.SEASONAL
        properties[WorldProperty.DEADMAN] -> HiscoreEndpoint.DEADMAN
        else -> HiscoreEndpoint.NORMAL
    }

    val properties: BitVec get() = BitVec(CLIENT.worldProperties)

    val hiscoreEndpoint: HiscoreEndpoint get() = hiscoreEndpoint(properties)
}
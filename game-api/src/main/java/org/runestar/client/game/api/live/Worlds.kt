package org.runestar.client.game.api.live

import com.google.common.collect.Iterators
import io.reactivex.Observable
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.World
import org.runestar.client.game.raw.CLIENT

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
}
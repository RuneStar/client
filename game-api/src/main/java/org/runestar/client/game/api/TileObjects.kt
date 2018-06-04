package org.runestar.client.game.api

import io.reactivex.Observable
import org.runestar.client.game.api.utils.cascadingListOf
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.game.raw.access.XTile
import java.util.*

sealed class TileObjects<T>(val accessor: XScene) {

    abstract fun at(sceneTile: SceneTile): List<T>

    fun onPlane(plane: Int): Sequence<T> = onPlanes(plane, plane)

    fun all(): Sequence<T> = onPlanes(0, Scene.PLANE_SIZE - 1)

    abstract fun onPlanes(planeStart: Int, planeEndInclusive: Int): Sequence<T>

    protected fun getTile(plane: Int, x: Int, y: Int): XTile? = accessor.tiles[plane][x][y]

    abstract val additions: Observable<T>

    abstract val removals: Observable<T>

    abstract class Single<T>(accessor: XScene) : TileObjects<T>(accessor) {

        internal abstract fun fromTile(tile: XTile): T?

        operator fun get(sceneTile: SceneTile): T? {
            require(sceneTile.isLoaded) { sceneTile }
            val tile = getTile(sceneTile.plane, sceneTile.x, sceneTile.y) ?: return null
            return fromTile(tile)
        }

        override fun at(sceneTile: SceneTile): List<T> {
            require(sceneTile.isLoaded) { sceneTile }
            return cascadingListOf(get(sceneTile))
        }

        override fun onPlanes(planeStart: Int, planeEndInclusive: Int): Sequence<T> = Sequence {
            object : AbstractIterator<T>() {

                private var plane = planeStart

                private var y = 0

                private var x = 0

                override fun computeNext() {
                    var found = false
                    while (!found) {
                        getTile(plane, x, y)?.let {
                            fromTile(it)?.let {
                                found = true
                                setNext(it)
                            }
                        }
                        if (y == Scene.SIZE - 1) {
                            if (x == Scene.SIZE - 1) {
                                if (plane == planeEndInclusive) {
                                    return done()
                                } else {
                                    y = 0
                                    x = 0
                                    plane++
                                }
                            } else {
                                y = 0
                                x++
                            }
                        } else {
                            y++
                        }
                    }
                }
            }
        }
    }

    abstract class Many<T>(accessor: XScene) : TileObjects<T>(accessor) {

        internal abstract fun fromTile(tile: XTile): List<T>

        operator fun get(sceneTile: SceneTile): List<T> {
            require(sceneTile.isLoaded) { sceneTile }
            val tile = getTile(sceneTile.plane, sceneTile.x, sceneTile.y) ?: return emptyList()
            return fromTile(tile)
        }

        override fun at(sceneTile: SceneTile): List<T> = get(sceneTile)

        override fun onPlanes(planeStart: Int, planeEndInclusive: Int): Sequence<T> = Sequence {
            object : AbstractIterator<T>() {

                private var itr: Iterator<T> = Collections.emptyIterator()

                private var plane = planeStart

                private var y = 0

                private var x = 0

                override fun computeNext() {
                    if (itr.hasNext()) {
                        return setNext(itr.next())
                    }
                    var found = false
                    while (!found) {
                        getTile(plane, x, y)?.let {
                            itr = fromTile(it).iterator()
                            if (itr.hasNext()) {
                                found = true
                                setNext(itr.next())
                            }
                        }
                        if (y == Scene.SIZE - 1) {
                            if (x == Scene.SIZE - 1) {
                                if (plane == planeEndInclusive) {
                                    return done()
                                } else {
                                    y = 0
                                    x = 0
                                    plane++
                                }
                            } else {
                                y = 0
                                x++
                            }
                        } else {
                            y++
                        }
                    }
                }
            }
        }
    }
}
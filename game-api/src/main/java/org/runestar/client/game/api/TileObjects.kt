package org.runestar.client.game.api

import com.google.common.collect.Iterators
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.game.raw.access.XTile
import java.util.*

sealed class TileObjects<T>(val accessor: XScene) {

    abstract fun at(sceneTile: SceneTile): Iterable<T>

    fun onPlane(plane: Int): Iterable<T> {
        return onPlanes(plane, plane)
    }

    fun all(): Iterable<T> {
        return onPlanes(0, Scene.PLANE_SIZE - 1)
    }

    abstract fun onPlanes(planeStart: Int, planeEndInclusive: Int): Iterable<T>

    protected fun getTile(plane: Int, x: Int, y: Int): XTile? {
        return accessor.tiles[plane][x][y]
    }

    abstract class Single<T>(accessor: XScene) : TileObjects<T>(accessor) {

        protected abstract fun fromTile(tile: XTile): T?

        operator fun get(sceneTile: SceneTile): T? {
            require(sceneTile.isLoaded) { sceneTile }
            val tile = getTile(sceneTile.plane, sceneTile.x, sceneTile.y) ?: return null
            return fromTile(tile)
        }

        override fun at(sceneTile: SceneTile): Iterable<T> {
            require(sceneTile.isLoaded) { sceneTile }
            return Iterable<T> {
                val e = get(sceneTile)
                if (e == null) {
                    Collections.emptyIterator()
                } else {
                    Iterators.singletonIterator(e)
                }
            }
        }

        override fun onPlanes(planeStart: Int, planeEndInclusive: Int): Iterable<T> {
            return Iterable<T> {
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
    }

    abstract class Many<T>(accessor: XScene) : TileObjects<T>(accessor) {

        protected abstract fun fromTile(tile: XTile): Iterator<T>

        operator fun get(sceneTile: SceneTile): Iterable<T> {
            require(sceneTile.isLoaded) { sceneTile }
            return Iterable<T> {
                val tile = getTile(sceneTile.plane, sceneTile.x, sceneTile.y)
                if (tile == null) {
                    Collections.emptyIterator()
                } else {
                    fromTile(tile)
                }
            }
        }

        override fun at(sceneTile: SceneTile): Iterable<T> {
            return get(sceneTile)
        }

        override fun onPlanes(planeStart: Int, planeEndInclusive: Int): Iterable<T> {
            return Iterable<T> {
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
                                itr = fromTile(it)
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
}
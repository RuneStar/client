package com.runesuite.client.game.api

import com.hunterwb.gridnav.AStar
import com.hunterwb.gridnav.Cell
import com.hunterwb.gridnav.Distance

interface PathFinder {

    fun findPath(source: SceneTile, destination: SceneTile): List<SceneTile>?

    class Walking(val scene: Scene) : PathFinder {

        override fun findPath(source: SceneTile, destination: SceneTile): List<SceneTile>? {
            require(source.plane == destination.plane)
            val movement = WalkMovement(scene, source.plane)
            val s = Cell(source.x, source.y)
            val d = Cell(destination.x, destination.y)
            // todo : JumpPoint broken
            val jp = AStar(movement, Distance.Octile.CHEBYSHEV, Distance.Octile.CHEBYSHEV, s)
            return jp.findPath(d)?.map { SceneTile(it.x, it.y, source.plane) }
        }
    }
}
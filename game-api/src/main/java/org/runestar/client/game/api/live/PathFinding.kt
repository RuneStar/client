package org.runestar.client.game.api.live

import org.runestar.client.game.api.PathFinder

object PathFinding {

    val walking: PathFinder = PathFinder.Walking(LiveScene)

    val projectile: PathFinder = PathFinder.Projectile(LiveScene)
}
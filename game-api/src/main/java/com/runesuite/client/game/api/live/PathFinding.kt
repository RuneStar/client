package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.PathFinder

object PathFinding {

    val walking: PathFinder = PathFinder.Walking(LiveScene)

    val projectile: PathFinder = PathFinder.Projectile(LiveScene)
}
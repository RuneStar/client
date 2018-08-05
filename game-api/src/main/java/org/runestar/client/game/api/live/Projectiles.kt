package org.runestar.client.game.api.live

import io.reactivex.Observable
import org.runestar.client.game.api.NodeDeque
import org.runestar.client.game.api.Position
import org.runestar.client.game.api.Projectile
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XProjectile

object Projectiles : NodeDeque<Projectile, XProjectile>(CLIENT.projectiles) {

    override fun wrap(n: XProjectile): Projectile = Projectile(n)

    val destinationChanges: Observable<Pair<Projectile, Position>> = XProjectile.setDestination.enter.map {
        val localX = it.arguments[0] as Int
        val localY = it.arguments[1] as Int
        val height = it.arguments[2] as Int // todo
        val projectile = Projectile(it.instance)
        projectile to Position(localX, localY, height, projectile.plane)
    }
}
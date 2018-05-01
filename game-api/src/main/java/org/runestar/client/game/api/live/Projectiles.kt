package org.runestar.client.game.api.live

import io.reactivex.Observable
import org.runestar.client.game.api.Position
import org.runestar.client.game.api.Projectile
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XNode
import org.runestar.client.game.raw.access.XProjectile

object Projectiles : Iterable<Projectile> {

    override fun iterator() = object : AbstractIterator<Projectile>() {

        private var cur: XNode? = Client.accessor.projectiles.sentinel.previous

        override fun computeNext() {
            val p = cur as? XProjectile ?: return done()
            setNext(Projectile(p))
            cur = p.previous
        }
    }

    val adjustments: Observable<Pair<Projectile, Position>> = XProjectile.adjust.enter.map {
        val localX = it.arguments[0] as Int
        val localY = it.arguments[1] as Int
        val height = it.arguments[2] as Int // todo
        val projectile = Projectile(it.instance)
        projectile to Position(localX, localY, height, projectile.plane)
    }
}
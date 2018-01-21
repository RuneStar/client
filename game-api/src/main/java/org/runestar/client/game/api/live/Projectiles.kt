package org.runestar.client.game.api.live

import org.runestar.client.game.api.Projectile
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.game.raw.access.XProjectile

object Projectiles : Iterable<Projectile> {

    override fun iterator() = object : Iterator<Projectile> {
        private var n = accessor.projectiles.sentinel.previous
        override fun hasNext() = n != accessor.projectiles.sentinel
        override fun next() = Projectile(n as XProjectile).also { n = n.previous }
    }

    fun isEmpty() = accessor.projectiles.sentinel.previous == accessor.projectiles.sentinel
}
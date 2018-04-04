package org.runestar.client.game.api.live

import org.runestar.client.game.api.Projectile
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.Client.accessor
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
}
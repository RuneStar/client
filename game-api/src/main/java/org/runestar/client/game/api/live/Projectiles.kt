package org.runestar.client.game.api.live

import org.runestar.client.game.api.Projectile
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.game.raw.access.XProjectile

object Projectiles {

    val all: List<Projectile> get() {
        val nodes = accessor.projectiles
        val list = ArrayList<Projectile>()
        var node = nodes.sentinel.previous
        while (node != nodes.sentinel) {
            list.add(Projectile(node as XProjectile))
            node = node.previous
        }
        return list
    }
}
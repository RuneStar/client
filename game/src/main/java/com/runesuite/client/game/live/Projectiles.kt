package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.base.access.XProjectile
import com.runesuite.client.game.Projectile
import java.util.*

object Projectiles {

    val all: Sequence<Projectile> get() {
        val nodes = accessor.projectiles
        val list = ArrayList<XProjectile>()
        var node = nodes.last() as XProjectile?
        while (node != null) {
            list.add(node)
            node = nodes.previous() as XProjectile?
        }
        return list.asSequence().map { Projectile(it) }
    }
}
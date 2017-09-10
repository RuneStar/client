package com.runesuite.client.core.api.live

import com.runesuite.client.core.raw.Client.accessor
import com.runesuite.client.core.raw.access.XProjectile

object Projectiles {

    val all: List<com.runesuite.client.core.api.Projectile> get() {
        val nodes = accessor.projectiles
        val list = ArrayList<com.runesuite.client.core.api.Projectile>()
        var node = nodes.sentinel.previous
        while (node != nodes.sentinel) {
            list.add(com.runesuite.client.core.api.Projectile(node as XProjectile))
            node = node.previous
        }
        return list
    }
}
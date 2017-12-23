package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2

class KeyHandler : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(java.awt.event.KeyListener::class.type) }

    class keyPressed : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.name == "keyPressed" }
    }
}
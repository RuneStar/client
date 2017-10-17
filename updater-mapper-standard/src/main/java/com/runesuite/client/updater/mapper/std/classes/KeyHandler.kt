package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Method2

class KeyHandler : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(java.awt.event.KeyListener::class.type) }

    class keyPressed : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.name == "keyPressed" }
    }
}
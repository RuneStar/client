package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Method2
import org.runestar.client.updater.mapper.mark
import java.awt.event.KeyListener

class KeyHandler : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(KeyListener::class.type) }

    @MethodParameters("keyEvent")
    class keyPressed : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == KeyListener::keyPressed.mark }
    }

    @MethodParameters("keyEvent")
    class keyReleased : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == KeyListener::keyReleased.mark }
    }

    @MethodParameters("keyEvent")
    class keyTyped : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == KeyListener::keyTyped.mark }
    }
}
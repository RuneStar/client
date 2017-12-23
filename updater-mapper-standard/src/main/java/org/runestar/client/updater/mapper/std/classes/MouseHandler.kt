package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.mark
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

class MouseHandler : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(MouseListener::class.type) }

    class mouseMoved : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == MouseMotionListener::mouseMoved.mark }
    }

    class mousePressed : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == MouseListener::mousePressed.mark }
    }
}
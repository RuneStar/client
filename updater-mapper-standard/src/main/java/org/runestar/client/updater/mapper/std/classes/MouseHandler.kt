package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.mark
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Method2
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

    @MethodParameters("mouseEvent")
    class getButton : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
    }
}
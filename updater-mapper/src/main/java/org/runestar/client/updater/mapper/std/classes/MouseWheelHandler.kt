package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import org.runestar.client.updater.mapper.mark
import java.awt.event.MouseListener
import java.awt.event.MouseWheelListener

class MouseWheelHandler : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(MouseWheelListener::class.type) }

    class rotation : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    @MethodParameters()
    @DependsOn(MouseWheel.useRotation::class)
    class useRotation : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<MouseWheel.useRotation>().mark }
    }

    @MethodParameters("component")
    class addTo : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodName == "addMouseWheelListener" } }
    }

    @MethodParameters("component")
    class removeFrom : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodName == "removeMouseWheelListener" } }
    }

    @MethodParameters("mouseWheelEvent")
    class mouseWheelMoved : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == MouseWheelListener::mouseWheelMoved.mark }
    }
}
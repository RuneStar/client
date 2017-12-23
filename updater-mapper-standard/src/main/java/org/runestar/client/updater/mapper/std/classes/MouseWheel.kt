package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Type

@SinceVersion(141)
@DependsOn(MouseWheelHandler::class)
class MouseWheel : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.type in klass<MouseWheelHandler>().interfaces }

    @MethodParameters()
    class useRotation : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.INT_TYPE }
    }
}

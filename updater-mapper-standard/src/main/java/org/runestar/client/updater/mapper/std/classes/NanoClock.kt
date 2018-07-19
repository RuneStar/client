package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(Clock::class)
class NanoClock : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Clock>() }
            .and { it.instanceFields.size == 1 }

    class lastTimeNano : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { true }
    }

    @MethodParameters()
    class mark : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.VOID_TYPE }
    }

    @MethodParameters("cycleMs", "minSleepMs")
    class wait : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.INT_TYPE }
    }
}
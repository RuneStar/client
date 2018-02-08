package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2

@SinceVersion(162)
@DependsOn(Buddy::class)
class Friend : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Buddy>() }
            .and { it.instanceFields.count { it.type == INT_TYPE } >= 2 }
            .and { it.instanceFields.count { it.type == BOOLEAN_TYPE } == 2 }

    @MethodParameters("other")
    class compareTo00 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.startsWith(type<Friend>()) }
    }
}
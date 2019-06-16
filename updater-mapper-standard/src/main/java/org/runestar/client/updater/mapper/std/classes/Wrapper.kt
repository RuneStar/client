package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import java.lang.reflect.Modifier

@DependsOn(DualNode::class)
class Wrapper : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { Modifier.isAbstract(it.access) }
            .and { it.superType == type<DualNode>() }
            .and { it.instanceMethods.any { it.returnType == Any::class.type } }

    @MethodParameters()
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Any::class.type }
    }

    @MethodParameters()
    class isSoft : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
    }

    class size : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }
}
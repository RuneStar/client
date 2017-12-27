package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(Node::class, IterableNodeDeque::class)
class HealthBar : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.size == 2 }
            .and { it.instanceFields.count { it.type == type<IterableNodeDeque>() } == 1 }
            .and { it.instanceFields.all { it.type in it.jar } }

    @DependsOn(IterableNodeDeque::class)
    class definition : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type != type<IterableNodeDeque>() }
    }

    @DependsOn(IterableNodeDeque::class)
    class hitSplats : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeDeque>() }
    }

    @MethodParameters()
    class isEmpty : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
    }

    @MethodParameters("cycle")
    @DependsOn(HitSplat::class)
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<HitSplat>() }
    }

//    @MethodParameters
    class put : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
    }
}
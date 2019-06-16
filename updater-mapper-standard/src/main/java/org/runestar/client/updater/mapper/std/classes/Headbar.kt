package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2

@DependsOn(Node::class, IterableNodeDeque::class)
class Headbar : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.size == 2 }
            .and { it.instanceFields.count { it.type == type<IterableNodeDeque>() } == 1 }
            .and { it.instanceFields.all { it.type in it.jar } }

    @DependsOn(IterableNodeDeque::class)
    class type : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type != type<IterableNodeDeque>() }
    }

    @DependsOn(IterableNodeDeque::class)
    class updates : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeDeque>() }
    }

    @MethodParameters()
    class isEmpty : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
    }

    @MethodParameters("cycle")
    @DependsOn(HeadbarUpdate::class)
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<HeadbarUpdate>() }
    }

//    @MethodParameters
    class put : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
    }
}
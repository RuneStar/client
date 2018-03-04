package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.extensions.type
import java.lang.reflect.Modifier

class Node : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.instanceFields.size == 3 }
            .and { it.instanceFields.count { it.type == LONG_TYPE } == 1 }
            .and { it.superType == Any::class.type }
            .and { c -> c.instanceFields.count { it.type == c.type } == 2 }

    class key : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == LONG_TYPE }
    }

    class previous : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Node>() }
                .and { Modifier.isPublic(it.access) }
    }

    class next : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Node>() }
                .and { !Modifier.isPublic(it.access) }
    }

    @MethodParameters()
    class hasNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
    }

    @MethodParameters()
    class remove : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
    }
}
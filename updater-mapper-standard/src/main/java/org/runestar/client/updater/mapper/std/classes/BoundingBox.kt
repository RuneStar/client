package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Type.VOID_TYPE
import java.lang.reflect.Modifier

@SinceVersion(141)
@DependsOn(Node::class)
class BoundingBox : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { Modifier.isAbstract(it.access) }
            .and { it.superType == type<Node>() }
            .and { it.instanceFields.isEmpty() }
            .and { it.instanceMethods.size == 1 }
            .and { it.instanceMethods.all { it.returnType == VOID_TYPE && it.arguments.size in 0..1 } }

    @MethodParameters()
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
    }
}
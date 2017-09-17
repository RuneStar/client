package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Method2
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
package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import java.lang.reflect.Modifier

@DependsOn(CacheNode::class)
class Entity : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<CacheNode>() }
            .and { it.instanceFields.size == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 1 }
            .and { Modifier.isAbstract(it.access) }

    class height : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    @MethodParameters
    class getModel : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType in it.jar }
    }

    class renderAtPoint : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
    }
}
package com.runesuite.client.updater.map.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

@DependsOn(Node::class)
class CacheNode : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.size == 2 }
            .and { c -> c.instanceFields.count { it.type == c.type } == 2 }

    @MethodParameters()
    class cacheRemove : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.VOID_TYPE }
    }

    @DependsOn(cacheRemove::class)
    class cacheNext : OrderMapper.InMethod.Field(cacheRemove::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.GETFIELD }
    }

    @DependsOn(cacheNext::class)
    class cachePrevious : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.id != field<cacheNext>().id }
    }
}
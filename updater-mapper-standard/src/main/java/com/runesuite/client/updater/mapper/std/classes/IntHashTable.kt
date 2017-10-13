package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes

@DependsOn(AbstractIndexCache.setIndexReference::class)
class IntHashTable : OrderMapper.InMethod.Class(AbstractIndexCache.setIndexReference::class, 1, 3) {
    override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.NEW && it.typeType in it.jar }

    class array : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { true }
    }

    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { true }
    }
}
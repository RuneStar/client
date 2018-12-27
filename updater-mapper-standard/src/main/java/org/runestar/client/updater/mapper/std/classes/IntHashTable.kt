package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
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
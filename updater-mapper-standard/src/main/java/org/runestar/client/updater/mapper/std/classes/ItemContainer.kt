package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.PUTFIELD

@DependsOn(Node::class)
class ItemContainer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.count { it.type == IntArray::class.type } == 2 }
            .and { it.instanceFields.size == 2 }

    class ids : OrderMapper.InConstructor.Field(ItemContainer::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class quantities : OrderMapper.InConstructor.Field(ItemContainer::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }
}
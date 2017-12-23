package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.extensions.withDimensions
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE

class CollisionMap : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 4 }
            .and { it.instanceFields.count { it.type == INT_TYPE.withDimensions(2) } == 1 }
            .and { it.instanceFields.size == 5 }

    class flags : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE.withDimensions(2) }
    }

    class xInset : OrderMapper.InConstructor.Field(CollisionMap::class, 0, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class yInset : OrderMapper.InConstructor.Field(CollisionMap::class, 1, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class xSize : OrderMapper.InConstructor.Field(CollisionMap::class, -2, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    class ySize : OrderMapper.InConstructor.Field(CollisionMap::class, -1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }
}
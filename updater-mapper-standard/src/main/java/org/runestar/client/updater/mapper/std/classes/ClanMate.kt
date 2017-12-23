package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.StaticUniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.nextWithin
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type.BYTE_TYPE
import org.objectweb.asm.Type.INT_TYPE

@DependsOn(Node::class)
class ClanMate : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.count { it.type == String::class.type } == 2 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 1 }
            .and { it.instanceFields.count { it.type == BYTE_TYPE } == 1 }
            .and { it.instanceMethods.isEmpty() }

    class rank : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BYTE_TYPE }
    }

    class world : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    class name : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.SIPUSH && it.intOperand == 3613 }
                .nextWithin(40) { it.opcode == Opcodes.GETFIELD && it.fieldOwner == type<ClanMate>() && it.fieldType == String::class.type }
    }

    @DependsOn(name::class)
    class name2 : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type && it != field<name>() }
    }
}
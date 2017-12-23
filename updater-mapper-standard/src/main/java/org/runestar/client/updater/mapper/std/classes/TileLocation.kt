package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.mark
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.VOID_TYPE

@SinceVersion(141)
class TileLocation : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.size == 3 }
            .and { it.instanceFields.all { it.type == INT_TYPE } }
            .and { it.instanceMethods.any { it.mark == Any::hashCode.mark } }

    class set : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 3..4 }
    }

    @DependsOn(set::class)
    class plane : OrderMapper.InMethod.Field(set::class, 0, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(set::class)
    class x : OrderMapper.InMethod.Field(set::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(set::class)
    class y : OrderMapper.InMethod.Field(set::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == INT_TYPE }
    }
}
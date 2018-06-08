package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2

@SinceVersion(164)
@DependsOn(Buddy::class)
class ClanMate : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Buddy>() }
            .and { it.instanceFields.count { it.type == BOOLEAN_TYPE } == 0 }

    @DependsOn(TriBool::class)
    class isFriend : OrderMapper.InConstructor.Field(ClanMate::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == type<TriBool>() }
    }

    @DependsOn(TriBool::class)
    class isIgnored : OrderMapper.InConstructor.Field(ClanMate::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == type<TriBool>() }
    }
}
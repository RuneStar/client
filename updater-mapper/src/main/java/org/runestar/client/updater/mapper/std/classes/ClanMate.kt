package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2

@DependsOn(Buddy::class)
class ClanMate : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Buddy>() }
            .and { it.instanceFields.count { it.type == BOOLEAN_TYPE } == 0 }

    @DependsOn(TriBool::class)
    class isFriend0 : OrderMapper.InConstructor.Field(ClanMate::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<TriBool>() }
    }

    @DependsOn(TriBool::class)
    class isIgnored0 : OrderMapper.InConstructor.Field(ClanMate::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<TriBool>() }
    }

    @MethodParameters()
    @DependsOn(isFriend0::class)
    class isFriend : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE && it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<isFriend0>().id } }
    }

    @MethodParameters()
    @DependsOn(isIgnored0::class)
    class isIgnored : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE && it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<isIgnored0>().id } }
    }

    @MethodParameters()
    @DependsOn(isFriend::class)
    class fillIsFriend : UniqueMapper.InMethod.Method(isFriend::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @MethodParameters()
    @DependsOn(isIgnored::class)
    class fillIsIgnored : UniqueMapper.InMethod.Method(isIgnored::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @MethodParameters()
    @DependsOn(isFriend0::class, Client.TriBool_unknown::class)
    class clearIsFriend : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.VOID_TYPE && it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<isFriend0>().id } }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<Client.TriBool_unknown>().id } }
    }

    @MethodParameters()
    @DependsOn(isIgnored0::class, Client.TriBool_unknown::class)
    class clearIsIgnored : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.VOID_TYPE && it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<isIgnored0>().id } }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<Client.TriBool_unknown>().id } }
    }
}
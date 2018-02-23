package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@SinceVersion(164)
@DependsOn(UserList::class, ClanMate::class)
class ClanChat : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<UserList>() }
            .and { it.instanceMethods.flatMap { it.instructions.toList() }.any { it.opcode == NEW && it.typeType == type<ClanMate>() } }

    @DependsOn(UserList.newInstance::class)
    class newInstance : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<UserList.newInstance>().mark }
    }

    @DependsOn(UserList.newTypedArray::class)
    class newTypedArray : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<UserList.newTypedArray>().mark }
    }

    @DependsOn(LoginType::class)
    class loginType : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<LoginType>() }
    }

    class name : OrderMapper.InConstructor.Field(ClanChat::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    class owner : OrderMapper.InConstructor.Field(ClanChat::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }
}
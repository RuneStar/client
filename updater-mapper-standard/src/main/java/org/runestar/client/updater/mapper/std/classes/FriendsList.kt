package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2

@SinceVersion(164)
@DependsOn(UserList::class, Friend::class)
class FriendsList : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<UserList>() }
            .and { it.instanceMethods.flatMap { it.instructions.toList() }.any { it.opcode == NEW && it.typeType == type<Friend>() } }

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

    @DependsOn(LinkDeque::class)
    class friendLoginUpdates : IdentityMapper.Field() {
        override val predicate = predicateOf<Field2> { it.type == type<LinkDeque>() }
    }

    @MethodParameters("buffer", "n")
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.VOID_TYPE }
    }
}
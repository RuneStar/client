package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2

@SinceVersion(162)
@DependsOn(BuddyList::class, Ignored::class)
class IgnoreList : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<BuddyList>() }
            .and { it.instanceMethods.flatMap { it.instructions.toList() }.any { it.opcode == NEW && it.typeType == type<Ignored>() } }

    @DependsOn(BuddyList.newInstance::class)
    class newInstance : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<BuddyList.newInstance>().mark }
    }

    @DependsOn(BuddyList.newTypedArray::class)
    class newTypedArray : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<BuddyList.newTypedArray>().mark }
    }

    @DependsOn(LoginType::class)
    class loginType : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<LoginType>() }
    }
}
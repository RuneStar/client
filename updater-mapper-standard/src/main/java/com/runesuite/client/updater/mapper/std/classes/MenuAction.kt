package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.StaticOrderMapper
import com.runesuite.mapper.StaticUniqueMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.nextWithin
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

class MenuAction : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.count { it.type == String::class.type } == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 4 }
            .and { it.instanceFields.size == 5 }

    @SinceVersion(134)
    @DependsOn(Client.menuArguments1::class)
    class argument1 : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<MenuAction>() }
                .nextWithin(50) { it.opcode == GETSTATIC && it.fieldId == field<Client.menuArguments1>().id }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldOwner == type<MenuAction>() && it.fieldType == INT_TYPE }
    }

    @SinceVersion(134)
    @DependsOn(Client.menuArguments2::class)
    class argument2 : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<MenuAction>() }
                .nextWithin(50) { it.opcode == GETSTATIC && it.fieldId == field<Client.menuArguments2>().id }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldOwner == type<MenuAction>() && it.fieldType == INT_TYPE }
    }

    @SinceVersion(134)
    @DependsOn(Client.menuOpcodes::class)
    class opcode : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<MenuAction>() }
                .nextWithin(50) { it.opcode == GETSTATIC && it.fieldId == field<Client.menuOpcodes>().id }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldOwner == type<MenuAction>() && it.fieldType == INT_TYPE }
    }

    @SinceVersion(134)
    @DependsOn(Client.menuArguments0::class)
    class argument0 : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<MenuAction>() }
                .nextWithin(50) { it.opcode == GETSTATIC && it.fieldId == field<Client.menuArguments0>().id }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldOwner == type<MenuAction>() && it.fieldType == INT_TYPE }
    }

    @SinceVersion(134)
    @DependsOn(Client.menuActions::class)
    class action : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<MenuAction>() }
                .nextWithin(50) { it.opcode == GETSTATIC && it.fieldId == field<Client.menuActions>().id }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldOwner == type<MenuAction>() && it.fieldType == String::class.type }
    }
}
package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.StaticUniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.nextWithin
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Instruction2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

class MiniMenuEntry : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.count { it.type == String::class.type } == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 4 }
            .and { it.instanceFields.size == 5 }

    @DependsOn(Client.menuArguments1::class)
    class argument1 : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<MiniMenuEntry>() }
                .nextWithin(50) { it.opcode == GETSTATIC && it.fieldId == field<Client.menuArguments1>().id }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldOwner == type<MiniMenuEntry>() && it.fieldType == INT_TYPE }
    }

    @DependsOn(Client.menuArguments2::class)
    class argument2 : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<MiniMenuEntry>() }
                .nextWithin(50) { it.opcode == GETSTATIC && it.fieldId == field<Client.menuArguments2>().id }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldOwner == type<MiniMenuEntry>() && it.fieldType == INT_TYPE }
    }

    @DependsOn(Client.menuOpcodes::class)
    class opcode : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<MiniMenuEntry>() }
                .nextWithin(50) { it.opcode == GETSTATIC && it.fieldId == field<Client.menuOpcodes>().id }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldOwner == type<MiniMenuEntry>() && it.fieldType == INT_TYPE }
    }

    @DependsOn(Client.menuArguments0::class)
    class argument0 : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<MiniMenuEntry>() }
                .nextWithin(50) { it.opcode == GETSTATIC && it.fieldId == field<Client.menuArguments0>().id }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldOwner == type<MiniMenuEntry>() && it.fieldType == INT_TYPE }
    }

    @DependsOn(Client.menuActions::class)
    class action : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<MiniMenuEntry>() }
                .nextWithin(50) { it.opcode == GETSTATIC && it.fieldId == field<Client.menuActions>().id }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldOwner == type<MiniMenuEntry>() && it.fieldType == String::class.type }
    }
}
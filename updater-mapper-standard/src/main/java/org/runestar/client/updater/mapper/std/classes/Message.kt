package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2

@DependsOn(DualNode::class)
class Message : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.size >= 6 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 3 }
            .and { it.instanceFields.count { it.type == String::class.type } == 3 }

    @DependsOn(set::class)
    class sender : OrderMapper.InMethod.Field(set::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    @DependsOn(set::class)
    class prefix : OrderMapper.InMethod.Field(set::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    @DependsOn(set::class)
    class text : OrderMapper.InMethod.Field(set::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    @DependsOn(set::class)
    class type : OrderMapper.InMethod.Field(set::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(set::class)
    class cycle : OrderMapper.InMethod.Field(set::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(set::class)
    class count : OrderMapper.InMethod.Field(set::class, 0, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(TriBool::class)
    class isFromFriend0 : OrderMapper.InConstructor.Field(Message::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<TriBool>() }
    }

    @DependsOn(TriBool::class)
    class isFromIgnored0 : OrderMapper.InConstructor.Field(Message::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<TriBool>() }
    }

    @MethodParameters("type", "sender", "prefix", "text")
    class set : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, String::class.type, String::class.type, String::class.type) }
    }

    @DependsOn(Username::class)
    class senderUsername : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Username>() }
    }

    @MethodParameters()
    @DependsOn(isFromFriend0::class)
    class isFromFriend : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE && it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<isFromFriend0>().id } }
    }

    @MethodParameters()
    @DependsOn(isFromIgnored0::class)
    class isFromIgnored : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE && it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<isFromIgnored0>().id } }
    }

    @MethodParameters()
    @DependsOn(isFromFriend::class)
    class fillIsFromFriend : UniqueMapper.InMethod.Method(isFromFriend::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @MethodParameters()
    @DependsOn(isFromIgnored::class)
    class fillIsFromIgnored : UniqueMapper.InMethod.Method(isFromIgnored::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @MethodParameters()
    @DependsOn(senderUsername::class)
    class fillSenderUsername : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE && it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<senderUsername>().id } }
    }

    @MethodParameters()
    @DependsOn(isFromFriend0::class, Client.TriBool_unknown::class)
    class clearIsFromFriend : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE && it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<isFromFriend0>().id } }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<Client.TriBool_unknown>().id } }
    }

    @MethodParameters()
    @DependsOn(isFromIgnored0::class, Client.TriBool_unknown::class)
    class clearIsFromIgnored : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE && it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<isFromIgnored0>().id } }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<Client.TriBool_unknown>().id } }
    }
}
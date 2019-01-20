package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.objectweb.asm.tree.JumpInsnNode
import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(Npc.definition::class)
class NpcDefinition : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { field<Npc.definition>().type == it.type }

    class name : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }

    class actions : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<String>::class.type }
    }

    @DependsOn(Buffer::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Buffer>()) }
                .and { it.instructions.none { it.opcode == BIPUSH && it.intOperand == 17 } }
    }

    @DependsOn(Buffer::class)
    class readNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Buffer>()) }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 17 } }
    }

    @DependsOn(Client.getNpcDefinition::class)
    class id : OrderMapper.InMethod.Field(Client.getNpcDefinition::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<NpcDefinition>() }
    }

    @MethodParameters("s1", "n1", "s2", "n2")
    @DependsOn(Model::class)
    class getModel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Model>() }
    }

    @DependsOn(NpcDefinition.getModel::class)
    class widthScale : OrderMapper.InMethod.Field(NpcDefinition.getModel::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(NpcDefinition.getModel::class)
    class heightScale : OrderMapper.InMethod.Field(NpcDefinition.getModel::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters()
    class transform : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<NpcDefinition>() }
    }

    @DependsOn(transform::class)
    class transforms : UniqueMapper.InMethod.Field(transform::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(transform::class)
    class transformVarbit : OrderMapper.InMethod.Field(transform::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(transform::class)
    class transformVarp : OrderMapper.InMethod.Field(transform::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(readNext::class)
    class combatLevel : UniqueMapper.InMethod.Field(readNext::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 95 }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(readNext::class)
    class drawMapDot : UniqueMapper.InMethod.Field(readNext::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 93 }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(readNext::class)
    class isInteractable : UniqueMapper.InMethod.Field(readNext::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 107 }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(readNext::class)
    class headIconPrayer : UniqueMapper.InMethod.Field(readNext::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 102 }
                .nextWithin(2) { it.node is JumpInsnNode }
                .nextWithin(12) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class size : OrderMapper.InConstructor.Field(NpcDefinition::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(getModel::class)
    class recolorFrom : OrderMapper.InMethod.Field(getModel::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(getModel::class)
    class recolorTo : OrderMapper.InMethod.Field(getModel::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(getModel::class)
    class retextureFrom : OrderMapper.InMethod.Field(getModel::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(getModel::class)
    class retextureTo : OrderMapper.InMethod.Field(getModel::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(readNext::class)
    class archives : OrderMapper.InMethod.Field(readNext::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class idleSequence : OrderMapper.InConstructor.Field(NpcDefinition::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class turnLeftSequence : OrderMapper.InConstructor.Field(NpcDefinition::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class turnRightSequence : OrderMapper.InConstructor.Field(NpcDefinition::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class walkSequence : OrderMapper.InConstructor.Field(NpcDefinition::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class walkTurnSequence : OrderMapper.InConstructor.Field(NpcDefinition::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class walkTurnLeftSequence : OrderMapper.InConstructor.Field(NpcDefinition::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class walkTurnRightSequence : OrderMapper.InConstructor.Field(NpcDefinition::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters()
    class init : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.isEmpty() }
    }

    @DependsOn(IterableNodeHashTable::class)
    class params : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeHashTable>() }
    }

    class getIntParam : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE) }
    }

    class getStringParam : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
                .and { it.arguments == listOf(INT_TYPE, String::class.type) }
    }

    @MethodParameters()
    @DependsOn(ModelData::class)
    class getModelData : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<ModelData>() }
    }

    class isFollower : OrderMapper.InConstructor.Field(NpcDefinition::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }
}
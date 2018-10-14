package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.extensions.withDimensions
import org.runestar.client.updater.mapper.prevIn
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(DualNode::class, NpcDefinition::class)
class ObjectDefinition : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.count { it.type == String::class.type } == 1 }
            .and { it.instanceFields.count { it.type == String::class.type.withDimensions(1) } == 1 }
            .and { it.instanceFields.count { it.type == ShortArray::class.type } == 4 }
            .and { it.type != type<NpcDefinition>() }

    class name : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }

    class actions : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type.withDimensions(1) }
    }

    @DependsOn(Buffer::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Buffer>()) }
                .and { it.instructions.none { it.opcode == BIPUSH && it.intOperand == 23 } }
    }

    @DependsOn(Buffer::class)
    class readNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Buffer>()) }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 23 } }
    }

    @DependsOn(Client.getObjectDefinition::class)
    class id : OrderMapper.InMethod.Field(Client.getObjectDefinition::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<ObjectDefinition>() }
    }

    @MethodParameters()
    class transform : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<ObjectDefinition>() }
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
    class transformConfigId : OrderMapper.InMethod.Field(transform::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    class sizeX : OrderMapper.InConstructor.Field(ObjectDefinition::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class sizeY : OrderMapper.InConstructor.Field(ObjectDefinition::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class interactType : OrderMapper.InConstructor.Field(ObjectDefinition::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class boolean1 : OrderMapper.InConstructor.Field(ObjectDefinition::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class int1 : OrderMapper.InConstructor.Field(ObjectDefinition::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class clipType : OrderMapper.InConstructor.Field(ObjectDefinition::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class nonFlatShading : OrderMapper.InConstructor.Field(ObjectDefinition::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class modelClipped : OrderMapper.InConstructor.Field(ObjectDefinition::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class animationId : OrderMapper.InConstructor.Field(ObjectDefinition::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int2 : OrderMapper.InConstructor.Field(ObjectDefinition::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class ambient : OrderMapper.InConstructor.Field(ObjectDefinition::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class contrast : OrderMapper.InConstructor.Field(ObjectDefinition::class, 8) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class mapIconId : OrderMapper.InConstructor.Field(ObjectDefinition::class, 9) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class mapSceneId : OrderMapper.InConstructor.Field(ObjectDefinition::class, 10) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class isRotated : OrderMapper.InConstructor.Field(ObjectDefinition::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class clipped : OrderMapper.InConstructor.Field(ObjectDefinition::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class modelSizeX : OrderMapper.InConstructor.Field(ObjectDefinition::class, 11) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class modelHeight : OrderMapper.InConstructor.Field(ObjectDefinition::class, 12) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class modelSizeY : OrderMapper.InConstructor.Field(ObjectDefinition::class, 13) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class offsetX : OrderMapper.InConstructor.Field(ObjectDefinition::class, 14) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class offsetHeight : OrderMapper.InConstructor.Field(ObjectDefinition::class, 15) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class offsetY : OrderMapper.InConstructor.Field(ObjectDefinition::class, 16) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class boolean2 : OrderMapper.InConstructor.Field(ObjectDefinition::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class isSolid : OrderMapper.InConstructor.Field(ObjectDefinition::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class int3 : OrderMapper.InConstructor.Field(ObjectDefinition::class, 17) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class ambientSoundId : OrderMapper.InConstructor.Field(ObjectDefinition::class, 20) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int4 : OrderMapper.InConstructor.Field(ObjectDefinition::class, 21) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int5 : OrderMapper.InConstructor.Field(ObjectDefinition::class, 22) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int6 : OrderMapper.InConstructor.Field(ObjectDefinition::class, 23) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters()
    @DependsOn(Client.getObjectDefinition::class)
    class init : OrderMapper.InMethod.Method(Client.getObjectDefinition::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<ObjectDefinition>() }
    }

    @DependsOn(Model::class)
    class getModel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Model>() }
                .and { it.arguments.size in 6..7 }
    }

    @DependsOn(Model::class)
    class getModelDynamic : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Model>() }
                .and { it.arguments.size in 8..9 }
    }

    @MethodParameters("n", "m")
    @DependsOn(ModelData::class)
    class getModelData : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<ModelData>() }
    }

    @DependsOn(getModelData::class)
    class recolorFrom : OrderMapper.InMethod.Field(getModelData::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(getModelData::class)
    class recolorTo : OrderMapper.InMethod.Field(getModelData::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(getModelData::class)
    class retextureFrom : OrderMapper.InMethod.Field(getModelData::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(getModelData::class)
    class retextureTo : OrderMapper.InMethod.Field(getModelData::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
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
}
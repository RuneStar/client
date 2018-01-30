package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.*

@DependsOn(DualNode::class)
class SpotAnimationDefinition : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.count { it.type == ShortArray::class.type } == 4 }
            .and { it.instanceFields.count { it.type == INT_TYPE } >= 8 }
            .and { it.instanceFields.all { it.type == INT_TYPE || it.type == ShortArray::class.type } }

    class readNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 40 } }
    }

    @DependsOn(readNext::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it != method<readNext>() }
    }

    @DependsOn(Client.getSpotAnimationDefinition::class)
    class id : OrderMapper.InMethod.Field(Client.getSpotAnimationDefinition::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<SpotAnimationDefinition>() }
    }

    @DependsOn(Model::class)
    class getModel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Model>() }
    }

    // in degrees, 0, 180, 270
    @DependsOn(SpotAnimationDefinition.getModel::class)
    class orientation : OrderMapper.InMethod.Field(SpotAnimationDefinition.getModel::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(readNext::class)
    class widthScale : UniqueMapper.InMethod.Field(readNext::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_4 }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(readNext::class)
    class heightScale : UniqueMapper.InMethod.Field(readNext::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_5 }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class sequence : OrderMapper.InConstructor.Field(SpotAnimationDefinition::class, 0) {
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
}
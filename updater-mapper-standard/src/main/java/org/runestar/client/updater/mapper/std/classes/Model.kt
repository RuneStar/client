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
import org.runestar.client.updater.mapper.withDimensions
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2

@DependsOn(Entity.getModel::class)
class Model : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.type == method<Entity.getModel>().returnType }

    class verticesX : OrderMapper.InConstructor.Field(Model::class, 0) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.isNotEmpty() }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class verticesY : OrderMapper.InConstructor.Field(Model::class, 1) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.isNotEmpty() }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class verticesZ : OrderMapper.InConstructor.Field(Model::class, 2) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.isNotEmpty() }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class indices1 : OrderMapper.InConstructor.Field(Model::class, 3) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.isNotEmpty() }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class indices2 : OrderMapper.InConstructor.Field(Model::class, 4) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.isNotEmpty() }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class indices3 : OrderMapper.InConstructor.Field(Model::class, 5) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.isNotEmpty() }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @MethodParameters("yaw", "cameraPitchSine", "cameraPitchCosine", "cameraYawSine", "cameraYawCosine", "x", "y", "z", "tag")
    @DependsOn(Entity.draw::class)
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Entity.draw>().mark }
    }

    @MethodParameters("pitch")
    @DependsOn(Projectile.getModel::class)
    class rotateZ : OrderMapper.InMethod.Method(Projectile.getModel::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<Model>() }
    }

    class draw0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(BOOLEAN_TYPE, BOOLEAN_TYPE, BOOLEAN_TYPE) }
    }

    class verticesCount : OrderMapper.InConstructor.Field(Model::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
        override val constructorPredicate = predicateOf<Method2> { it.arguments.isEmpty() }
    }

    class indicesCount : OrderMapper.InConstructor.Field(Model::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
        override val constructorPredicate = predicateOf<Method2> { it.arguments.isEmpty() }
    }

    // either xz or xyz
    @DependsOn(Model.draw::class)
    class xzRadius : OrderMapper.InMethod.Field(Model.draw::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters("x", "y", "z")
    @DependsOn(Npc.getModel::class)
    class offset : OrderMapper.InMethod.Method(Npc.getModel::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL && it.methodOwner == type<Model>() }
    }

    // yaw
    @MethodParameters()
    @DependsOn(Player.getModel::class)
    class rotateY90Ccw : OrderMapper.InMethod.Method(Player.getModel::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL && it.methodOwner == type<Model>() && it.methodType.argumentTypes.size in 0..1 }
    }

    @MethodParameters()
    @DependsOn(rotateZ::class)
    class resetBounds : UniqueMapper.InMethod.Method(rotateZ::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    // new = old * scale / 128
    @MethodParameters("x", "y", "z")
    class resize : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 3..4 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == SIPUSH && it.intOperand == 128 } == 3 }
    }

    @MethodParameters()
    @DependsOn(draw::class)
    class calculateBoundsCylinder : OrderMapper.InMethod.Method(draw::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @MethodParameters("yaw")
    @DependsOn(draw::class)
    class calculateBoundingBox : OrderMapper.InMethod.Method(draw::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @DependsOn(draw::class)
    class boundsType : OrderMapper.InMethod.Field(draw::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(calculateBoundsCylinder::class)
    class bottomY : OrderMapper.InMethod.Field(calculateBoundsCylinder::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(calculateBoundsCylinder::class)
    class radius : OrderMapper.InMethod.Field(calculateBoundsCylinder::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(calculateBoundsCylinder::class)
    class diameter : OrderMapper.InMethod.Field(calculateBoundsCylinder::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(calculateBoundingBox::class)
    class xMid : OrderMapper.InMethod.Field(calculateBoundingBox::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(calculateBoundingBox::class)
    class yMid : OrderMapper.InMethod.Field(calculateBoundingBox::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(calculateBoundingBox::class)
    class zMid : OrderMapper.InMethod.Field(calculateBoundingBox::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(calculateBoundingBox::class)
    class xMidOffset : OrderMapper.InMethod.Field(calculateBoundingBox::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(calculateBoundingBox::class)
    class yMidOffset : OrderMapper.InMethod.Field(calculateBoundingBox::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(calculateBoundingBox::class)
    class zMidOffset : OrderMapper.InMethod.Field(calculateBoundingBox::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Model::class, Player.getModel::class)
    class isSingleTile : UniqueMapper.InMethod.Field(Player.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldOwner == type<Model>() && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(Model::class)
    class copy0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Model>() }
                .and { it.arguments.startsWith(BOOLEAN_TYPE, type<Model>(), ByteArray::class.type) }
    }

    @MethodParameters("type", "labels", "tx", "ty", "tz")
    class transform : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 5..6 }
                .and { it.arguments.startsWith(INT_TYPE, IntArray::class.type, INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @DependsOn(transform::class)
    class vertexLabels : OrderMapper.InMethod.Field(transform::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE.withDimensions(2) }
    }

    @DependsOn(transform::class)
    class faceLabelsAlpha : OrderMapper.InMethod.Field(transform::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE.withDimensions(2) }
    }

    @MethodParameters("frames", "frame")
    @DependsOn(AnimFrameset::class)
    class animate : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(type<AnimFrameset>(), INT_TYPE) }
    }

    @DependsOn(AnimFrameset::class)
    class animate2 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 5..6 }
                .and { it.arguments.startsWith(type<AnimFrameset>(), INT_TYPE, type<AnimFrameset>(), INT_TYPE, IntArray::class.type) }
    }

    @MethodParameters("b")
    @DependsOn(SpotType.getModel::class)
    class toSharedSpotAnimationModel : UniqueMapper.InMethod.Method(SpotType.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<Model>() && it.methodType.returnType == type<Model>() }
    }

    @MethodParameters("b")
    @DependsOn(NPCType.getModel::class)
    class toSharedSequenceModel : UniqueMapper.InMethod.Method(NPCType.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<Model>() && it.methodType.returnType == type<Model>() }
    }

    @DependsOn(transform::class)
    class faceAlphas : UniqueMapper.InMethod.Field(transform::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == ByteArray::class.type }
    }

    @MethodParameters()
    class rotateY180 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.count { it.opcode == INEG } == 2 }
                .and { it.instructions.count { it.opcode == IASTORE } == 2 }
    }

    @MethodParameters()
    @DependsOn(rotateY90Ccw::class)
    class rotateY270Ccw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.count { it.opcode == INEG } == 1 }
                .and { it != method<rotateY90Ccw>() }
    }

    @DependsOn(UnlitModel.light::class)
    class faceColors1 : OrderMapper.InMethod.Field(UnlitModel.light::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldOwner == type<Model>() && it.fieldType == IntArray::class.type }
    }

    @DependsOn(UnlitModel.light::class)
    class faceColors2 : OrderMapper.InMethod.Field(UnlitModel.light::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldOwner == type<Model>() && it.fieldType == IntArray::class.type }
    }

    @DependsOn(UnlitModel.light::class)
    class faceColors3 : OrderMapper.InMethod.Field(UnlitModel.light::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldOwner == type<Model>() && it.fieldType == IntArray::class.type }
    }

    @DependsOn(UnlitModel.light::class)
    class faceTextures : OrderMapper.InMethod.Field(UnlitModel.light::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldOwner == type<Model>() && it.fieldType == ShortArray::class.type }
    }
}
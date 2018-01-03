package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

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

    @DependsOn(Entity.draw::class)
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Entity.draw>().mark }
    }

    @MethodParameters("pitch")
    @DependsOn(Projectile.getModel::class)
    class rotateZ : OrderMapper.InMethod.Method(Projectile.getModel::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<Model>() }
    }

    class method0 : IdentityMapper.InstanceMethod() {
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
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.GETFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters("x", "y", "z")
    @DependsOn(Npc.getModel::class)
    class offsetBy : OrderMapper.InMethod.Method(Npc.getModel::class, 1) {
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
    class scale : IdentityMapper.InstanceMethod() {
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
}
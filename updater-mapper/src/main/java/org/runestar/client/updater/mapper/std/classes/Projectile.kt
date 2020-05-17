package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2

@DependsOn(Entity::class)
class Projectile : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Entity>() }
            .and { it.instanceFields.count { it.type == DOUBLE_TYPE } >= 8 }

    class id : OrderMapper.InConstructor.Field(Projectile::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(SeqType::class)
    class seqType : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<SeqType>() }
    }

    @MethodParameters()
    @DependsOn(Entity.getModel::class)
    class getModel : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Entity.getModel>().mark }
    }

    @MethodParameters("cycles")
    class advance : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
    }

    @MethodParameters("x", "y", "height", "cycle")
    class setDestination : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @DependsOn(getModel::class)
    class pitch : OrderMapper.InMethod.Field(getModel::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    class sourceX : OrderMapper.InConstructor.Field(Projectile::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class sourceY : OrderMapper.InConstructor.Field(Projectile::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class sourceZ : OrderMapper.InConstructor.Field(Projectile::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class plane : OrderMapper.InConstructor.Field(Projectile::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class targetIndex : OrderMapper.InConstructor.Field(Projectile::class, 11) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class isMoving : OrderMapper.InConstructor.Field(Projectile::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(advance::class)
    class x : OrderMapper.InMethod.Field(advance::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == DOUBLE_TYPE }
    }

    @DependsOn(advance::class)
    class y : OrderMapper.InMethod.Field(advance::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == DOUBLE_TYPE }
    }

    @DependsOn(advance::class)
    class z : OrderMapper.InMethod.Field(advance::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == DOUBLE_TYPE }
    }

    @DependsOn(advance::class)
    class speedX : OrderMapper.InMethod.Field(advance::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == DOUBLE_TYPE }
    }

    @DependsOn(advance::class)
    class speedY : OrderMapper.InMethod.Field(advance::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == DOUBLE_TYPE }
    }

    @DependsOn(advance::class)
    class speedZ : OrderMapper.InMethod.Field(advance::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == DOUBLE_TYPE }
    }

    @DependsOn(advance::class)
    class yaw : OrderMapper.InMethod.Field(advance::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(setDestination::class)
    class speed : OrderMapper.InMethod.Field(setDestination::class, -3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == DOUBLE_TYPE }
    }

    @DependsOn(setDestination::class)
    class accelerationZ : OrderMapper.InMethod.Field(setDestination::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == DOUBLE_TYPE }
    }

    class cycleStart : OrderMapper.InConstructor.Field(Projectile::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class cycleEnd : OrderMapper.InConstructor.Field(Projectile::class, 8) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int3 : OrderMapper.InConstructor.Field(Projectile::class, 9) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int4 : OrderMapper.InConstructor.Field(Projectile::class, 10) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int5 : OrderMapper.InConstructor.Field(Projectile::class, 12) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class frameCycle : OrderMapper.InConstructor.Field(Projectile::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class frame : OrderMapper.InConstructor.Field(Projectile::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}
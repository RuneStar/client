package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.extensions.*
import java.lang.reflect.Modifier

@DependsOn(Entity::class)
class Actor : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Entity>() }
            .and { Modifier.isAbstract(it.access) }

    @MethodParameters()
    class isVisible : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
    }

    @DependsOn(IterableNodeDeque::class)
    class healthBars : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeDeque>() }
    }

    class overheadText : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }

    class pathX : OrderMapper.InConstructor.Field(Actor::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 10 }
                .next { it.opcode == NEWARRAY && it.intOperand == 10 }
                .next { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class pathY : OrderMapper.InConstructor.Field(Actor::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 10 }
                .next { it.opcode == NEWARRAY && it.intOperand == 10 }
                .next { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class targetIndex : OrderMapper.InConstructor.Field(Actor::class, 12) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class sequence : OrderMapper.InConstructor.Field(Actor::class, 17) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class pathLength : OrderMapper.InConstructor.Field(Actor::class, -3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class pathTraversed : OrderMapper.InConstructor.Field(Actor::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == ByteArray::class.type }
    }

    // ?
    class standAnimation : OrderMapper.InConstructor.Field(Actor::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    // ?
    class runAnimation : OrderMapper.InConstructor.Field(Actor::class, 14) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    // ?
    class animationDelay : OrderMapper.InConstructor.Field(Actor::class, 20) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class spotAnimation : OrderMapper.InConstructor.Field(Actor::class, 22) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class npcCycle : OrderMapper.InConstructor.Field(Actor::class, 25) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class x : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.SIPUSH && it.intOperand == 3308 }
                .nextWithin(15) { it.opcode == Opcodes.GETFIELD && it.fieldType == INT_TYPE }

        override fun resolve(instruction: Instruction2): Field2 {
            return instruction.jar[type<Actor>() to instruction.fieldName]
        }
    }

    @DependsOn(x::class)
    class y : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.SIPUSH && it.intOperand == 3308 }
                .nextWithin(25) { it.opcode == Opcodes.GETFIELD && it.fieldType == INT_TYPE && it.fieldName != field<x>().name }

        override fun resolve(instruction: Instruction2): Field2 {
            return instruction.jar[type<Actor>() to instruction.fieldName]
        }
    }

    class orientation : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == Math::atan2.id }
                .nextWithin(6) { it.opcode == SIPUSH && it.intOperand == 2047 }
                .nextWithin(6) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Actor>() }
    }

    class overheadTextCyclesRemaining : OrderMapper.InConstructor.Field(Actor::class, 9) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Npc.getModel::class, Model.offsetBy::class)
    class heightOffset : UniqueMapper.InMethod.Field(Npc.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Model.offsetBy>().id }
                .prevWithin(8) { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
        override fun resolve(instruction: Instruction2): Field2 {
            return instruction.jar[type<Actor>() to instruction.fieldName]
        }
    }
}
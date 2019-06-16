package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.std.ActorHitmarkField
import org.runestar.client.updater.mapper.std.ConstructorPutField
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
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
    class headbars : InstanceField() {
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

    class movementFrame : OrderMapper.InConstructor.Field(Actor::class, 15) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class movementFrameCycle : OrderMapper.InConstructor.Field(Actor::class, 16) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class sequence : OrderMapper.InConstructor.Field(Actor::class, 17) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class sequenceFrame : OrderMapper.InConstructor.Field(Actor::class, 18) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class sequenceFrameCycle : OrderMapper.InConstructor.Field(Actor::class, 19) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class pathLength : OrderMapper.InConstructor.Field(Actor::class, -3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class pathTraversed : OrderMapper.InConstructor.Field(Actor::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == ByteArray::class.type }
    }

    class size : OrderMapper.InConstructor.Field(Actor::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class readySequence : OrderMapper.InConstructor.Field(Actor::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class turnLeftSequence : OrderMapper.InConstructor.Field(Actor::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class turnRightSequence : OrderMapper.InConstructor.Field(Actor::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class walkSequence : OrderMapper.InConstructor.Field(Actor::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class walkBackSequence : OrderMapper.InConstructor.Field(Actor::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class walkLeftSequence : OrderMapper.InConstructor.Field(Actor::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class walkRightSequence : OrderMapper.InConstructor.Field(Actor::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class runSequence : OrderMapper.InConstructor.Field(Actor::class, 8) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class movementSequence : OrderMapper.InConstructor.Field(Actor::class, 14) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class sequenceDelay : OrderMapper.InConstructor.Field(Actor::class, 20) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class spotAnimation : OrderMapper.InConstructor.Field(Actor::class, 22) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class spotAnimationFrame : OrderMapper.InConstructor.Field(Actor::class, 23) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class spotAnimationFrameCycle : OrderMapper.InConstructor.Field(Actor::class, 24) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class npcCycle : OrderMapper.InConstructor.Field(Actor::class, 25) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class x : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3308 }
                .nextWithin(15) { it.opcode == Opcodes.GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(x::class)
    class y : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3308 }
                .nextWithin(25) { it.opcode == Opcodes.GETFIELD && it.fieldType == INT_TYPE && it.fieldName != field<x>().name }
    }

    @DependsOn(Player.getModel::class, Model.offsetBy::class)
    class orientation : UniqueMapper.InMethod.Field(Player.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Model.offsetBy>().id }
                .nextWithin(5) { it.opcode == GETFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Actor>() }
    }

    class overheadTextCyclesRemaining : OrderMapper.InConstructor.Field(Actor::class, 9) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Npc.getModel::class, Model.offsetBy::class)
    class heightOffset : UniqueMapper.InMethod.Field(Npc.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Model.offsetBy>().id }
                .prevWithin(8) { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Npc.getModel::class, Npc::class)
    class defaultHeight : UniqueMapper.InMethod.Field(Npc.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Actor>() }
    }

    // spotAnimationStartCycle

    @DependsOn(Client.addPlayerToScene::class)
    class playerCycle : OrderMapper.InMethod.Field(Client.addPlayerToScene::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldOwner == type<Actor>() && it.fieldType == INT_TYPE }
    }

    @MethodParameters("headbarType")
    @DependsOn(Headbar::class)
    class removeHeadbar : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == CHECKCAST && it.typeType == type<Headbar>() } }
    }

    @MethodParameters("headbarType", "cycle", "n0", "n1", "n2", "n3")
    @DependsOn(Headbar::class)
    class addHeadbar : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 6..7 }
                .and { it.instructions.any { it.opcode == CHECKCAST && it.typeType == type<Headbar>() } }
    }

    class hitmarkTypes : ActorHitmarkField(0)
    class hitmarkValues : ActorHitmarkField(1)
    class hitmarkCycles : ActorHitmarkField(2)
    class hitmarkTypes2 : ActorHitmarkField(3)
    class hitmarkValues2 : ActorHitmarkField(4)

    @MethodParameters("type", "value", "type2", "value2", "cycle", "cycle2")
    class addHitmark : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 6..7 }
                .and { it.instructions.any { it.opcode == IALOAD }  }
    }

    class hitmarkCount : ConstructorPutField(Actor::class, 0, BYTE_TYPE)

    @DependsOn(Actor.overheadTextCyclesRemaining::class)
    class isAutoChatting : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldId == field<overheadTextCyclesRemaining>().id }
                .prev { it.opcode == SIPUSH && it.intOperand == 150 }
                .prevWithin(9) { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(Actor.overheadTextCyclesRemaining::class)
    class overheadTextEffect : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldId == field<overheadTextCyclesRemaining>().id }
                .prev { it.opcode == SIPUSH && it.intOperand == 150 }
                .prevWithin(2) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Actor.overheadTextCyclesRemaining::class)
    class overheadTextColor : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldId == field<overheadTextCyclesRemaining>().id }
                .prev { it.opcode == SIPUSH && it.intOperand == 150 }
                .prevWithin(2) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
                .prevWithin(3) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class false0 : OrderMapper.InConstructor.Field(Actor::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }
}
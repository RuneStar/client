package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2

@DependsOn(Actor::class)
class Player : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Actor>() }
            .and { it.instanceFields.size > 1 }

    @DependsOn(Username::class)
    class username : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Username>() }
    }

    class actions : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<String>::class.type }
    }

    @DependsOn(Model::class)
    class model0 : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Model>() }
    }

    @DependsOn(PlayerAppearance::class)
    class appearance : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<PlayerAppearance>() }
    }

    @MethodParameters()
    @DependsOn(Actor.isVisible::class)
    class isVisible : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Actor.isVisible>().mark }
    }

    @MethodParameters()
    class transformedSize : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
    }

    @MethodParameters
    @DependsOn(Entity.getModel::class)
    class getModel : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Entity.getModel>().mark }
    }

    class headIconPk : OrderMapper.InConstructor.Field(Player::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class headIconPrayer : OrderMapper.InConstructor.Field(Player::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class combatLevel : OrderMapper.InConstructor.Field(Player::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class skillLevel : OrderMapper.InConstructor.Field(Player::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    // todo
    class animationCycleStart : OrderMapper.InConstructor.Field(Player::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    // todo
    class animationCycleEnd : OrderMapper.InConstructor.Field(Player::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class isUnanimated : OrderMapper.InConstructor.Field(Player::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(Client.updatePlayer::class)
    class index : OrderMapper.InMethod.Field(Client.updatePlayer::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD }
    }

    @DependsOn(Model.offset::class, Player.getModel::class)
    class tileHeight : OrderMapper.InMethod.Field(getModel::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Model.offset>().id }
                .prevWithin(6) { it.opcode == ISUB }
                .prevWithin(10) { it.opcode == ISUB }
                .prevWithin(10) { it.opcode == ISUB }
                .nextWithin(5) { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    class team : OrderMapper.InConstructor.Field(Player::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class isHidden : OrderMapper.InConstructor.Field(Player::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(Client.plane::class, Client.updatePlayer::class)
    class plane : UniqueMapper.InMethod.Field(Client.updatePlayer::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<Client.plane>().id }
                .prevWithin(5) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Model.offset::class, getModel::class)
    class tileHeight2 : OrderMapper.InMethod.Field(getModel::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Model.offset>().id }
                .prevWithin(6) { it.opcode == ISUB }
                .prevWithin(10) { it.opcode == ISUB }
                .prevWithin(5) { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters("x", "y")
    @DependsOn(transformedSize::class)
    class resetPath : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.isMethod && it.methodId == method<transformedSize>().id } }
    }

    @DependsOn(resetPath::class)
    class tileY : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL && it.methodId == method<resetPath>().id }
                .prevWithin(4) { it.opcode == GETFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Player>() }
    }

    @DependsOn(resetPath::class)
    class tileX : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL && it.methodId == method<resetPath>().id }
                .prevWithin(4) { it.opcode == GETFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Player>() }
                .prevWithin(6) { it.opcode == GETFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Player>() }
    }

    @DependsOn(TriBool::class)
    class isFriend : OrderMapper.InConstructor.Field(Player::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<TriBool>() }
    }

    @DependsOn(TriBool::class)
    class isInClanChat : OrderMapper.InConstructor.Field(Player::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<TriBool>() }
    }

    @MethodParameters("packet")
    @DependsOn(Packet::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Packet>()) }
    }
}
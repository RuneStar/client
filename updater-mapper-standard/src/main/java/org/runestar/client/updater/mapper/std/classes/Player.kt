package org.runestar.client.updater.mapper.std.classes

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
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.INT_TYPE

@DependsOn(Actor::class)
class Player : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Actor>() }
            .and { it.instanceFields.size > 1 }

    class name : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }

    class actions : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<String>::class.type }
    }

    @DependsOn(Model::class)
    class model0 : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Model>() }
    }

    @DependsOn(model0::class)
    class appearance : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type in it.jar }
                .and { it != field<model0>() }
    }

    @MethodParameters
    @DependsOn(Actor.isVisible::class)
    class isVisible : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Actor.isVisible>().mark }
    }

    @MethodParameters
    @DependsOn(Entity.getModel::class)
    class getModel : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Entity.getModel>().mark }
    }

    class skullIcon : OrderMapper.InConstructor.Field(Player::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class prayerIcon : OrderMapper.InConstructor.Field(Player::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class combatLevel : OrderMapper.InConstructor.Field(Player::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class team : OrderMapper.InConstructor.Field(Player::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class animationCycleStart : OrderMapper.InConstructor.Field(Player::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class animationCycleEnd : OrderMapper.InConstructor.Field(Player::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class isLowDetail : OrderMapper.InConstructor.Field(Player::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }
}
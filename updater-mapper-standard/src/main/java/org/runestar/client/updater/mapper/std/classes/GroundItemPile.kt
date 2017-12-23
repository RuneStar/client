package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@DependsOn(Entity::class)
class GroundItemPile : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<Entity>() } == 3 }

    @DependsOn(Scene.newGroundItemPile::class, Entity::class)
    class bottom : OrderMapper.InMethod.Field(Scene.newGroundItemPile::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.newGroundItemPile::class, Entity::class)
    class middle : OrderMapper.InMethod.Field(Scene.newGroundItemPile::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.newGroundItemPile::class, Entity::class)
    class top : OrderMapper.InMethod.Field(Scene.newGroundItemPile::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.newGroundItemPile::class)
    class x : OrderMapper.InMethod.Field(Scene.newGroundItemPile::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GroundItemPile>() }
    }

    @DependsOn(Scene.newGroundItemPile::class)
    class y : OrderMapper.InMethod.Field(Scene.newGroundItemPile::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GroundItemPile>() }
    }

    @DependsOn(Scene.newGroundItemPile::class)
    class tileHeight : OrderMapper.InMethod.Field(Scene.newGroundItemPile::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GroundItemPile>() }
    }

    @DependsOn(Scene.newGroundItemPile::class)
    class tag : OrderMapper.InMethod.Field(Scene.newGroundItemPile::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GroundItemPile>() }
    }

    @DependsOn(Scene.newGroundItemPile::class)
    class height : OrderMapper.InMethod.Field(Scene.newGroundItemPile::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GroundItemPile>() }
    }
}
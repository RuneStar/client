package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE

@DependsOn(Entity::class)
class BoundaryObject : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 7 }
            .and { it.instanceFields.count { it.type == type<Entity>() } == 2 }
            .and { it.instanceFields.size == 9 }
            .and { it.instanceMethods.isEmpty() }

    @DependsOn(Scene.newBoundaryObject::class)
    class tag : OrderMapper.InMethod.Field(Scene.newBoundaryObject::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.newBoundaryObject::class)
    class flags : OrderMapper.InMethod.Field(Scene.newBoundaryObject::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.newBoundaryObject::class)
    class x : OrderMapper.InMethod.Field(Scene.newBoundaryObject::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.newBoundaryObject::class)
    class y : OrderMapper.InMethod.Field(Scene.newBoundaryObject::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.newBoundaryObject::class)
    class tileHeight : OrderMapper.InMethod.Field(Scene.newBoundaryObject::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.newBoundaryObject::class, Entity::class)
    class entity1 : OrderMapper.InMethod.Field(Scene.newBoundaryObject::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.newBoundaryObject::class, Entity::class)
    class entity2 : OrderMapper.InMethod.Field(Scene.newBoundaryObject::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.newBoundaryObject::class)
    class orientationA : OrderMapper.InMethod.Field(Scene.newBoundaryObject::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.newBoundaryObject::class)
    class orientationB : OrderMapper.InMethod.Field(Scene.newBoundaryObject::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}
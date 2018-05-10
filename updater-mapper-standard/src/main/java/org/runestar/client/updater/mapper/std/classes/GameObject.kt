package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.LONG_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.std.ConstructorPutField
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2

@SinceVersion(170)
@DependsOn(Entity::class)
class GameObject : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.count { it.type == INT_TYPE } >= 12 }
            .and { it.instanceFields.count { it.type == type<Entity>() } == 1 }

    @DependsOn(Entity::class)
    class entity : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Entity>() }
    }

    class tag : ConstructorPutField(GameObject::class, 0, LONG_TYPE)

    @DependsOn(Scene.newGameObject::class)
    class flags : OrderMapper.InMethod.Field(Scene.newGameObject::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.newGameObject::class)
    class plane : OrderMapper.InMethod.Field(Scene.newGameObject::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.newGameObject::class)
    class centerX : OrderMapper.InMethod.Field(Scene.newGameObject::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.newGameObject::class)
    class centerY : OrderMapper.InMethod.Field(Scene.newGameObject::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.newGameObject::class)
    class height : OrderMapper.InMethod.Field(Scene.newGameObject::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.newGameObject::class)
    class orientation : OrderMapper.InMethod.Field(Scene.newGameObject::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.newGameObject::class)
    class startX : OrderMapper.InMethod.Field(Scene.newGameObject::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.newGameObject::class)
    class startY : OrderMapper.InMethod.Field(Scene.newGameObject::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.newGameObject::class)
    class endX : OrderMapper.InMethod.Field(Scene.newGameObject::class, 8) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.newGameObject::class)
    class endY : OrderMapper.InMethod.Field(Scene.newGameObject::class, 9) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }
}

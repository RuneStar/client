package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE

@DependsOn(Entity::class)
class GameObject : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.count { it.type == INT_TYPE } >= 12 }
            .and { it.instanceFields.count { it.type == type<Entity>() } == 1 }

    @DependsOn(Entity::class)
    class entity : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Entity>() }
    }

    class id : OrderMapper.InConstructor.Field(GameObject::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setGameObject::class)
    class flags : OrderMapper.InMethod.Field(Scene.setGameObject::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.setGameObject::class)
    class plane : OrderMapper.InMethod.Field(Scene.setGameObject::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.setGameObject::class)
    class centerX : OrderMapper.InMethod.Field(Scene.setGameObject::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.setGameObject::class)
    class centerY : OrderMapper.InMethod.Field(Scene.setGameObject::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.setGameObject::class)
    class height : OrderMapper.InMethod.Field(Scene.setGameObject::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.setGameObject::class)
    class int6 : OrderMapper.InMethod.Field(Scene.setGameObject::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.setGameObject::class)
    class startX : OrderMapper.InMethod.Field(Scene.setGameObject::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.setGameObject::class)
    class startY : OrderMapper.InMethod.Field(Scene.setGameObject::class, 8) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.setGameObject::class)
    class endX : OrderMapper.InMethod.Field(Scene.setGameObject::class, 9) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }

    @DependsOn(Scene.setGameObject::class)
    class endY : OrderMapper.InMethod.Field(Scene.setGameObject::class, 10) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<GameObject>() }
    }
}
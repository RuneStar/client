package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.LONG_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Instruction2

@DependsOn(Entity::class)
class ObjStack : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<Entity>() } == 3 }

    @DependsOn(Scene.newObjStack::class, Entity::class)
    class first : OrderMapper.InMethod.Field(Scene.newObjStack::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.newObjStack::class, Entity::class)
    class second : OrderMapper.InMethod.Field(Scene.newObjStack::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.newObjStack::class, Entity::class)
    class third : OrderMapper.InMethod.Field(Scene.newObjStack::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.newObjStack::class)
    class x : OrderMapper.InMethod.Field(Scene.newObjStack::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<ObjStack>() }
    }

    @DependsOn(Scene.newObjStack::class)
    class y : OrderMapper.InMethod.Field(Scene.newObjStack::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<ObjStack>() }
    }

    @DependsOn(Scene.newObjStack::class)
    class tileHeight : OrderMapper.InMethod.Field(Scene.newObjStack::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<ObjStack>() }
    }

    @DependsOn(Scene.newObjStack::class)
    class tag : OrderMapper.InMethod.Field(Scene.newObjStack::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == LONG_TYPE && it.fieldOwner == type<ObjStack>() }
    }

    @DependsOn(Scene.newObjStack::class)
    class height : OrderMapper.InMethod.Field(Scene.newObjStack::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<ObjStack>() }
    }
}
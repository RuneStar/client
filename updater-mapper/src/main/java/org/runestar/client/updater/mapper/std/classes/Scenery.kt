package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.LONG_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.next
import org.runestar.client.updater.mapper.std.ConstructorPutField
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2

@DependsOn(Entity::class)
class Scenery : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.count { it.type == INT_TYPE } >= 12 }
            .and { it.instanceFields.count { it.type == type<Entity>() } == 1 }

    @DependsOn(Entity::class)
    class entity : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Entity>() }
    }

    class tag : ConstructorPutField(Scenery::class, 0, LONG_TYPE)

    @DependsOn(Scene.newScenery::class)
    class flags : OrderMapper.InMethod.Field(Scene.newScenery::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Scenery>() }
    }

    @DependsOn(Scene.newScenery::class)
    class plane : OrderMapper.InMethod.Field(Scene.newScenery::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Scenery>() }
    }

    @DependsOn(Scene.newScenery::class)
    class centerX : OrderMapper.InMethod.Field(Scene.newScenery::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Scenery>() }
    }

    @DependsOn(Scene.newScenery::class)
    class centerY : OrderMapper.InMethod.Field(Scene.newScenery::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Scenery>() }
    }

    @DependsOn(Scene.newScenery::class)
    class height : OrderMapper.InMethod.Field(Scene.newScenery::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Scenery>() }
    }

    @DependsOn(Scene.newScenery::class)
    class orientation : OrderMapper.InMethod.Field(Scene.newScenery::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Scenery>() }
    }

    @DependsOn(Scene.newScenery::class)
    class startX : OrderMapper.InMethod.Field(Scene.newScenery::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Scenery>() }
    }

    @DependsOn(Scene.newScenery::class)
    class startY : OrderMapper.InMethod.Field(Scene.newScenery::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Scenery>() }
    }

    @DependsOn(Scene.newScenery::class)
    class endX : OrderMapper.InMethod.Field(Scene.newScenery::class, 8) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Scenery>() }
    }

    @DependsOn(Scene.newScenery::class)
    class endY : OrderMapper.InMethod.Field(Scene.newScenery::class, 9) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Scenery>() }
    }

    @DependsOn(Scene.drawTile::class, Client.Scene_drawnCount::class)
    class lastDrawn : UniqueMapper.InMethod.Field(Scene.drawTile::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Client.Scene_drawnCount>().id }
                .next { it.opcode == PUTFIELD && it.fieldOwner == type<Scenery>() && it.fieldType == INT_TYPE }
    }
}

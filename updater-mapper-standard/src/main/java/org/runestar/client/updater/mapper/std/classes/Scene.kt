package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.arrayDimensions
import org.runestar.client.updater.mapper.baseType
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions

@DependsOn(GameObject::class)
class Scene : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.any { it.type == type<GameObject>().withDimensions(1) } }

    @DependsOn(GameObject::class)
    class tempGameObjects : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<GameObject>().withDimensions(1) }
    }

    @DependsOn(clear::class)
    class tempGameObjectsCount : OrderMapper.InMethod.Field(clear::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class tiles : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type.arrayDimensions == 3 && it.type.baseType in it.jar }
    }

    @MethodParameters("plane", "startX", "startY", "sizeX", "sizeY", "centerX", "centerY", "height", "entity", "orientation", "isTemp", "tag", "flags")
    @DependsOn(Entity::class)
    class newGameObject : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        INT_TYPE, INT_TYPE, INT_TYPE, type<Entity>(), INT_TYPE, BOOLEAN_TYPE, LONG_TYPE, INT_TYPE) }
                .and { it.arguments.size in 13..14 }
    }

    @MethodParameters("plane", "x", "y", "tileHeight", "entity", "tag", "flags")
    @DependsOn(Entity::class)
    class newFloorDecoration : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        type<Entity>(), LONG_TYPE, INT_TYPE) }
                .and { it.arguments.size in 7..8 }
    }

    @MethodParameters("plane", "x", "y", "tileHeight", "entity1", "entity2", "orientation", "int7", "xOffset", "yOffset", "tag", "flags")
    @DependsOn(Entity::class)
    class newWallDecoration : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        type<Entity>(), type<Entity>(), INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, LONG_TYPE, INT_TYPE) }
                .and { it.arguments.size in 12..13 }
    }

    @MethodParameters("plane", "x", "y", "tileHeight", "first", "tag", "second", "third")
    @DependsOn(Entity::class)
    class newObjStack : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        type<Entity>(), LONG_TYPE, type<Entity>(), type<Entity>()) }
                .and { it.arguments.size in 8..9 }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(Tile.objStack::class)
    class removeObjStack : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 3..4 }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<Tile.objStack>().id } }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(Tile.floorDecoration::class)
    class removeFloorDecoration : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 3..4 }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<Tile.floorDecoration>().id } }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(Tile.wallDecoration::class)
    class removeWallDecoration : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 3..4 }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<Tile.wallDecoration>().id } }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(Tile.boundaryObject::class)
    class removeBoundaryObject : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 3..4 }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<Tile.boundaryObject>().id } }
    }

    @MethodParameters("plane", "x", "y", "tileHeight", "entity1", "entity2", "orientationA", "orientationB", "tag", "flags")
    @DependsOn(Entity::class)
    class newBoundaryObject : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        type<Entity>(), type<Entity>(), INT_TYPE, INT_TYPE, LONG_TYPE, INT_TYPE) }
                .and { it.arguments.size in 10..11 }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(BoundaryObject::class)
    class getBoundaryObject : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<BoundaryObject>() }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(WallDecoration::class)
    class getWallDecoration : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<WallDecoration>() }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(FloorDecoration::class)
    class getFloorDecoration : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<FloorDecoration>() }
    }

    class planes : OrderMapper.InConstructor.Field(Scene::class, -3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class xSize : OrderMapper.InConstructor.Field(Scene::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class ySize : OrderMapper.InConstructor.Field(Scene::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class tileHeights : OrderMapper.InConstructor.Field(Scene::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE.withDimensions(3) }
    }

    @MethodParameters("x", "y", "z", "pitch", "yaw", "plane")
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 6..7 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("plane", "x", "y", "id")
    class getObjectFlags : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size == 4 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, LONG_TYPE) }
    }

    @MethodParameters("gameObject")
    @DependsOn(GameObject::class)
    class removeGameObject : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<GameObject>()) }
    }

    @MethodParameters()
    class clear : OrderMapper.InConstructor.Method(Scene::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @MethodParameters("minPlane")
    @DependsOn(Tile::class)
    class init : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size == 1 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.count { it.opcode == NEW && it.typeType == type<Tile>() } == 1 }
                .and { it.instructions.none { it.opcode == LAND } }
    }

    @DependsOn(init::class)
    class minPlane : UniqueMapper.InMethod.Field(init::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters()
    @DependsOn(tempGameObjects::class)
    class clearTempGameObjects : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.count { it.opcode == GETFIELD && it.fieldId == field<tempGameObjects>().id } == 2 }
    }

    @MethodParameters("pixels", "index", "width", "plane", "x", "y")
    class drawTileMinimap : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(FloorDecoration.tag::class)
    class getFloorDecorationTag : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == LONG_TYPE }
                .and { it.arguments.size in 3..4 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<FloorDecoration.tag>().id } }
                .and { it.instructions.none { it.opcode == LAND } }
    }

    @MethodParameters("x", "y")
    @DependsOn(Tile.linkedBelowTile::class)
    class setLinkBelow : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<Tile.linkedBelowTile>().id } }
    }

    class addTile : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size == 20 && it.arguments.all { it == INT_TYPE } }
    }

    @MethodParameters("tile", "b")
    @DependsOn(Tile::class)
    class drawTile : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<Tile>(), BOOLEAN_TYPE) }
    }

    @MethodParameters("plane", "x", "y", "minPlane")
    @DependsOn(Tile::class)
    class setTileMinPlane : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldOwner == type<Tile>() && it.fieldType == INT_TYPE } }
    }

    @MethodParameters()
    @DependsOn(Client.Scene_currentOccludersCount::class)
    class occlude : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == PUTSTATIC && it.fieldId == field<Client.Scene_currentOccludersCount>().id } }
    }

    @MethodParameters("plane", "screenX", "screenY", "b")
    class menuOpen : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE, BOOLEAN_TYPE) }
    }
}
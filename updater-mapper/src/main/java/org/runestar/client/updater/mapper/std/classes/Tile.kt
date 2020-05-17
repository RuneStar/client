package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.baseType
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions

@DependsOn(Node::class, Scene.tiles::class)
class Tile : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.type == field<Scene.tiles>().type.baseType }

    @DependsOn(Scenery::class)
    class scenery : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Scenery>().withDimensions(1) }
    }

    class plane : OrderMapper.InConstructor.Field(Tile::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class x : OrderMapper.InConstructor.Field(Tile::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class y : OrderMapper.InConstructor.Field(Tile::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(FloorDecoration::class)
    class floorDecoration : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<FloorDecoration>() }
    }

    @DependsOn(WallDecoration::class)
    class wallDecoration : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<WallDecoration>() }
    }

    @DependsOn(Wall::class)
    class wall : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Wall>() }
    }

    @DependsOn(ObjStack::class)
    class objStack : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<ObjStack>() }
    }

    @DependsOn(TileModel::class)
    class model : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<TileModel>() }
    }

    @DependsOn(TilePaint::class)
    class paint : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<TilePaint>() }
    }

    @DependsOn(Scene.newObjStack::class)
    class sceneryCount : UniqueMapper.InMethod.Field(Scene.newObjStack::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Tile>() }
    }

    class sceneryEdgeMasks : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == IntArray::class.type }
    }

    @DependsOn(Scene.removeScenery::class)
    class sceneryEdgeMask : OrderMapper.InMethod.Field(Scene.removeScenery::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Tile>() }
    }

    class linkedBelowTile : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Tile>() }
    }

    @DependsOn(Scene.setTileMinPlane::class)
    class minPlane : UniqueMapper.InMethod.Field(Scene.setTileMinPlane::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Tile>() }
    }

    @DependsOn(Scene.draw::class)
    class drawPrimary : OrderMapper.InMethod.Field(Scene.draw::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE && it.fieldOwner == type<Tile>() }
    }

    @DependsOn(Scene.draw::class)
    class drawSecondary : OrderMapper.InMethod.Field(Scene.draw::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE && it.fieldOwner == type<Tile>() }
    }

    @DependsOn(Scene.draw::class)
    class drawScenery : OrderMapper.InMethod.Field(Scene.draw::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE && it.fieldOwner == type<Tile>() }
    }

    class originalPlane : OrderMapper.InConstructor.Field(Tile::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class drawSceneryEdges : UniqueMapper.InMethod.Field(Scene.draw::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Tile>() }
    }
}
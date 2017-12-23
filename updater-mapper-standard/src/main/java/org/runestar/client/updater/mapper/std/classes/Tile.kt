package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.*
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import java.awt.image.TileObserver

@DependsOn(Node::class, Scene.tiles::class)
class Tile : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.type == field<Scene.tiles>().type.baseType }

    @DependsOn(GameObject::class)
    class gameObjects : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<GameObject>().withDimensions(1) }
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

    @DependsOn(BoundaryObject::class)
    class boundaryObject : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<BoundaryObject>() }
    }

    @DependsOn(GroundItemPile::class)
    class groundItemPile : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<GroundItemPile>() }
    }

    @DependsOn(TilePaint::class)
    class paint : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<TilePaint>() }
    }

    @DependsOn(TileModel::class)
    class model : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<TileModel>() }
    }

    @DependsOn(Scene.newGroundItemPile::class)
    class gameObjectsCount : UniqueMapper.InMethod.Field(Scene.newGroundItemPile::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Tile>() }
    }
}
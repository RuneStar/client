package com.runesuite.client.updater.map.classes

import com.hunterwb.kxtra.collections.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.arrayDimensions
import com.runesuite.mapper.extensions.baseType
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.extensions.withDimensions
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.VOID_TYPE

@DependsOn(GameObject::class)
class Scene : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.any { it.type == type<GameObject>().withDimensions(1) } }

    @DependsOn(GameObject::class)
    class gameObjects : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<GameObject>().withDimensions(1) }
    }

    class tiles : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type.arrayDimensions == 3 && it.type.baseType in it.jar }
    }

    @DependsOn(Entity::class)
    class newGameObject : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        INT_TYPE, INT_TYPE, INT_TYPE, type<Entity>(), INT_TYPE, BOOLEAN_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 13..14 }
    }

    @DependsOn(Entity::class)
    class newFloorDecoration : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        type<Entity>(), INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 7..8 }
    }

    @DependsOn(Entity::class)
    class newWallDecoration : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        type<Entity>(), type<Entity>(), INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 12..13 }
    }

    @MethodParameters("plane", "x", "y", "int0", "bottom", "int1", "middle", "top")
    @DependsOn(Entity::class)
    class newGroundItemPile : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        type<Entity>(), INT_TYPE, type<Entity>(), type<Entity>()) }
                .and { it.arguments.size in 8..9 }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(Tile.groundItemPile::class)
    class removeGroundItemPile : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 3..4 }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<Tile.groundItemPile>().id } }
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

    @DependsOn(Entity::class)
    class newBoundaryObject : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        type<Entity>(), type<Entity>(), INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
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
}
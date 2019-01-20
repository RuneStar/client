package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2
import java.lang.reflect.Modifier

@DependsOn(TileLocation::class)
class WorldMapSection : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { Modifier.isInterface(it.access) }
            .and { it.instanceMethods.any { it.returnType == type<TileLocation>() } }

    @MethodParameters("buffer")
    @DependsOn(Buffer::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<Buffer>()) }
    }

    @MethodParameters("area")
    @DependsOn(WorldMapArea::class)
    class expandBounds : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<WorldMapArea>()) }
    }

    @MethodParameters("plane", "x", "y")
    class position : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == IntArray::class.type }
    }

    @MethodParameters("x", "y")
    @DependsOn(TileLocation::class)
    class coord : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<TileLocation>() }
    }

    @MethodParameters("x", "y")
    class containsPosition : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size == 2 }
    }

    @MethodParameters("plane", "x", "y")
    class containsCoord : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size == 3 }
    }
}
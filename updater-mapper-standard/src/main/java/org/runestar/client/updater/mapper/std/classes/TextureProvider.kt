package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.withDimensions
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Type.*

@DependsOn(TextureLoader::class)
class TextureProvider : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.interfaces.contains(type<TextureLoader>()) }

    @DependsOn(NodeDeque::class)
    class deque : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<NodeDeque>() }
    }

    @DependsOn(Texture::class)
    class texture : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Texture>().withDimensions(1) }
    }

    class brightness : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == DOUBLE_TYPE }
    }

    @DependsOn(TextureLoader.load::class)
    class load : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<TextureLoader.load>().mark }
    }
}
package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.withDimensions
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
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
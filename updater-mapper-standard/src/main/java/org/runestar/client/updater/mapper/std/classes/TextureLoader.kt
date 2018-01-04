package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.annotations.DependsOn
import java.lang.reflect.Modifier

class TextureLoader : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { Modifier.isInterface(it.access) }
            .and { it.instanceMethods.size == 4 }
            .and { it.instanceMethods.count { it.returnType == BOOLEAN_TYPE } == 2 }

//    @MethodParameters()
    class load : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == IntArray::class.type }
    }

    @DependsOn(TextureProvider.isLowDetail::class)
    class isLowDetail : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<TextureProvider.isLowDetail>().mark }
    }
}
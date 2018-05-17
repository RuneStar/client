package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(StudioGame::class)
class Enumerated : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { klass<StudioGame>().interfaces.contains(it.type) }

    @MethodParameters()
    class ordinal : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.INT_TYPE }
    }
}
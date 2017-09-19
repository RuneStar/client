package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Type

@DependsOn(JagexGame::class)
class Enumerated : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { klass<JagexGame>().interfaces.contains(it.type) }

    class ordinal : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.INT_TYPE }
    }
}
package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2

@DependsOn(Timer::class)
class NanoTimer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Timer>() }
            .and { it.instanceFields.size == 1 }

    class nanoTime : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { true }
    }
}
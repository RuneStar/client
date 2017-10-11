package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2

@DependsOn(Timer::class)
class NanoTimer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Timer>() }
            .and { it.instanceFields.size == 1 }
}
package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

@DependsOn(Timer::class)
class NanoTimer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Timer>() }
            .and { it.instanceFields.size == 1 }

    class nanoTime : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { true }
    }
}
package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import org.runestar.client.common.startsWith

class GrandExchangeEvents : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.instanceFields.size == 1 }
            .and { it.instanceFields.all { it.type == List::class.type } }

    class events : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == List::class.type }
    }

    class sort : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.startsWith(Comparator::class.type) }
    }
}
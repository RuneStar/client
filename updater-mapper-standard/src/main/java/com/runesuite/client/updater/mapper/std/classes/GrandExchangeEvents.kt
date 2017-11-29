package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.kxtra.lang.list.startsWith

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
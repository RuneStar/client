package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(AbstractArchive::class)
class Fonts : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { it.instanceFields.size == 3 }
            .and { it.instanceFields.count { it.type == HashMap::class.type } == 1 }
            .and { it.instanceFields.count { it.type == type<AbstractArchive>() } == 2 }

    class map : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == HashMap::class.type }
    }

    class createMap : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == HashMap::class.type }
    }
}
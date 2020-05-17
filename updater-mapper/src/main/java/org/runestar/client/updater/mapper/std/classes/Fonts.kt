package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2

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
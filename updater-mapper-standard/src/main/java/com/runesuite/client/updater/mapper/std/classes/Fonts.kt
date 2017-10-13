package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2

@SinceVersion(141)
@DependsOn(AbstractIndexCache::class)
class Fonts : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { it.instanceFields.size == 3 }
            .and { it.instanceFields.count { it.type == HashMap::class.type } == 1 }
            .and { it.instanceFields.count { it.type == type<AbstractIndexCache>() } == 2 }

    class map : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == HashMap::class.type }
    }

    class createMap : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == HashMap::class.type }
    }
}
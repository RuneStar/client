package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2

@DependsOn(KeyChar::class, Enumerated::class)
class KeyChar0 : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<Enumerated>() != it }
            .and { klass<KeyChar>().interfaces.contains(it.type) }
}
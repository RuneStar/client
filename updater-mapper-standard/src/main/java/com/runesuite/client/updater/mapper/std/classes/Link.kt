package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Method2

class Link : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.instanceFields.size == 2 }
            .and { c -> c.instanceFields.all { it.type == c.type } }
            .and { it.instanceMethods.size == 1 }
            .and { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }

    class remove : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { true }
    }
}
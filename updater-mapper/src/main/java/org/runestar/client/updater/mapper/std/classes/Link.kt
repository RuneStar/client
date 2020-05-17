package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Method2

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
package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2

class TriBool : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.instanceFields.isEmpty() }
            .and { it.instanceMethods.isEmpty() }
            .and { it.superType == Any::class.type }
            .and { a -> a.staticFields.count { it.type == a.type } == 3 }
}
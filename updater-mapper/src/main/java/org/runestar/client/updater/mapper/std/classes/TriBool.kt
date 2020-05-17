package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import java.lang.reflect.Modifier

class TriBool : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.instanceFields.isEmpty() }
            .and { it.instanceMethods.isEmpty() }
            .and { it.superType == Any::class.type }
            .and { a -> a.staticFields.count { it.type == a.type } == 3 }
            .and { a -> a.staticFields.filter { it.type == a.type }.all { Modifier.isPublic(it.access) } }
}
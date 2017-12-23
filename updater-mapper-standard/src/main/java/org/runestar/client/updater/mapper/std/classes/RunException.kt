package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

class RunException : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == RuntimeException::class.type }

    class throwable : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Throwable::class.type }
    }

    class string : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }
}
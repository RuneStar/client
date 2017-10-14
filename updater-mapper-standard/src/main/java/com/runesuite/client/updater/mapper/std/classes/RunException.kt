package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2

class RunException : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == RuntimeException::class.type }

    class throwable : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Throwable::class.type }
    }

    class string : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }
}
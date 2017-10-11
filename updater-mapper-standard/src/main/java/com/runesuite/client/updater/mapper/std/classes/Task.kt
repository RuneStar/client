package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import java.lang.reflect.Modifier

class Task : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { Modifier.isVolatile(it.access) && it.type == Any::class.type } }

    class obj : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { Modifier.isVolatile(it.access) && it.type == Any::class.type }
    }

    class obj2 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { !Modifier.isVolatile(it.access) && it.type == Any::class.type }
    }

    class task : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Task>() }
    }
}
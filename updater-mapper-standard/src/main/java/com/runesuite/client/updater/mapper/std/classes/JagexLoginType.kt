package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import org.objectweb.asm.Type

// name length ??
class JagexLoginType : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and {  it.instanceFields.size == 2 }
            .and { it.instanceFields.count { it.type == Type.INT_TYPE } == 1 }
            .and { it.instanceFields.count { it.type == String::class.type } == 1 }
            .and { it.instanceMethods.size == 1 }
}
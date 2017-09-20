package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import org.objectweb.asm.Type
import java.lang.reflect.Modifier

class Timer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { Modifier.isAbstract(it.access) }
            .and { it.instanceFields.isEmpty() }
            .and { it.instanceMethods.size == 2 }
            .and { it.instanceMethods.any { it.returnType == Type.INT_TYPE } }
}
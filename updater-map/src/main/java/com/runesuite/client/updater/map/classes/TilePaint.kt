package com.runesuite.client.updater.map.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import org.objectweb.asm.Type

class TilePaint : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { it.superType == Any::class.type }
            .and { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.count { it.type == Type.BOOLEAN_TYPE } == 1 }
            .and { it.instanceFields.count { it.type == Type.INT_TYPE } == 4 }
            .and { it.instanceFields.count { it.type == IntArray::class.type } >= 6 }
}
package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import org.objectweb.asm.Type

class TileModel : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { it.superType == Any::class.type }
            .and { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.count { it.type == Type.INT_TYPE } == 6 }
            .and { it.instanceFields.count { it.type == Type.BOOLEAN_TYPE } == 1 }
            .and { it.instanceFields.size == 7 }
}
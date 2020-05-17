package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2

class WorldMapLabelSize : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.count { it.type == Type.INT_TYPE } == 3 }
            .and { it.instanceFields.size in 3..4 }
            .and { it.instanceMethods.any { it.arguments == listOf(Type.FLOAT_TYPE) && it.returnType == Type.BOOLEAN_TYPE } }
}
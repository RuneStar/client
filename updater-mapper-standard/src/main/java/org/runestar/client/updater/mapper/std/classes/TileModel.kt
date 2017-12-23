package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.objectweb.asm.Type

class TileModel : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { it.superType == Any::class.type }
            .and { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.count { it.type == Type.INT_TYPE } == 6 }
            .and { it.instanceFields.count { it.type == Type.BOOLEAN_TYPE } == 1 }
            .and { it.instanceFields.size == 7 }
}
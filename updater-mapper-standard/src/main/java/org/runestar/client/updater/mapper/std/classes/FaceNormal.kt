package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2

class FaceNormal : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.count() == 3 }
            .and { it.instanceFields.all { it.type == INT_TYPE } }
            .and { it.instanceMethods.isEmpty() }
            .and { it.constructors.size == 1 }
            .and { it.constructors.first().instructions.none { it.isField } }
}
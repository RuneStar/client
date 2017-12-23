package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.objectweb.asm.Type
import java.lang.reflect.Modifier

class Timer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { Modifier.isAbstract(it.access) }
            .and { it.instanceFields.isEmpty() }
            .and { it.instanceMethods.size == 2 }
            .and { it.instanceMethods.any { it.returnType == Type.INT_TYPE } }
}
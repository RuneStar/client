package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import java.lang.reflect.Modifier

@DependsOn(Node::class)
class SoundClass : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { Modifier.isAbstract(it.access) }
            .and { it.instanceFields.size == 1 }
            .and { it.instanceFields.all { it.type == Type.INT_TYPE } }
            .and { it.instanceMethods.size == 2 }
}
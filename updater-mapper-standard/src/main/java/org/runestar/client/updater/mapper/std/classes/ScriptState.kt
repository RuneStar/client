package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.objectweb.asm.Type.*

@DependsOn(Script::class)
class ScriptState : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<Script>() } == 1 }
            .and { it.instanceFields.count { it.type == Array<String>::class.type } == 1 }

    @DependsOn(Script::class)
    class script : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Script>() }
    }

    class strings : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<String>::class.type }
    }

    class int0 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    class ints : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == IntArray::class.type }
    }
}
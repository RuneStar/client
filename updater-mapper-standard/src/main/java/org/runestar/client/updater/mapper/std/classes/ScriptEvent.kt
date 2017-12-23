package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.*
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.objectweb.asm.Type.*

@DependsOn(Widget::class, Node::class)
class ScriptEvent : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { it.superType == type<Node>() }
            .and { it.instanceFields.count { it.type == type<Widget>() } == 2 }

    class string : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }

    class boolean1 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE }
    }

    class objs : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Any::class.type.withDimensions(1) }
    }
}
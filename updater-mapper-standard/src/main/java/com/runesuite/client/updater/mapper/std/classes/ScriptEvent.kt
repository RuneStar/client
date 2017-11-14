package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.*
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import org.objectweb.asm.Type.*

@DependsOn(Widget::class, Node::class)
class ScriptEvent : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { it.superType == type<Node>() }
            .and { it.instanceFields.count { it.type == type<Widget>() } == 2 }

    class string : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }

    class boolean : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE }
    }

    class objs : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Any::class.type.withDimensions(1) }
    }
}
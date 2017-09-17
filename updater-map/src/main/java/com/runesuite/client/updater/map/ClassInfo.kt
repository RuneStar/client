package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.extensions.withDimensions
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import org.objectweb.asm.Type

@DependsOn(Node::class)
class ClassInfo : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.any { it.type == Array<java.lang.reflect.Field>::class.type } }
            .and { it.instanceFields.any { it.type == Array<java.lang.reflect.Method>::class.type } }

    class fields : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<java.lang.reflect.Field>::class.type  }
    }

    class methods : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<java.lang.reflect.Method>::class.type  }
    }

    class bytes : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Type.BYTE_TYPE.withDimensions(3)  }
    }
}
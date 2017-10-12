package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import org.objectweb.asm.Type
import java.lang.reflect.Modifier

@DependsOn(Node::class)
class ByteArrayNode : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.size == 1 }
            .and { it.instanceFields.all { it.type == ByteArray::class.type } }
            .and { it.instanceMethods.isEmpty() }

    class byteArray : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteArray::class.type }
    }
}
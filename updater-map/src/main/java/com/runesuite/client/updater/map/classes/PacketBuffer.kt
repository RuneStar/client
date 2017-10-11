package com.runesuite.client.updater.map.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import org.objectweb.asm.Type

@DependsOn(Buffer::class)
class PacketBuffer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Buffer>() }

    class bitIndex : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Type.INT_TYPE }
    }

    class isaacCipher : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type in it.jar }
    }
}
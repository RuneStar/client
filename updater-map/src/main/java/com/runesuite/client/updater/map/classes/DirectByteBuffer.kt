package com.runesuite.client.updater.map.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import java.nio.ByteBuffer

@DependsOn(AbstractByteBuffer::class)
class DirectByteBuffer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<AbstractByteBuffer>() }

    @MethodParameters()
    @DependsOn(AbstractByteBuffer.get::class)
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractByteBuffer.get>().mark }
    }

    @MethodParameters("source")
    @DependsOn(AbstractByteBuffer.put::class)
    class put : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractByteBuffer.put>().mark }
    }

    class directNioBuffer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteBuffer::class.type }
    }
}
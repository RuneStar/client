package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import java.nio.ByteBuffer

@DependsOn(AbstractByteArrayCopier::class)
class DirectByteArrayCopier : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<AbstractByteArrayCopier>() }

    @MethodParameters()
    @DependsOn(AbstractByteArrayCopier.get::class)
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractByteArrayCopier.get>().mark }
    }

    @MethodParameters("array")
    @DependsOn(AbstractByteArrayCopier.set::class)
    class set : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractByteArrayCopier.set>().mark }
    }

    class directBuffer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteBuffer::class.type }
    }
}
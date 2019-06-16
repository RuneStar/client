package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
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
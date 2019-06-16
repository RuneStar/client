package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import java.security.SecureRandom
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class SecureRandomFuture : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceMethods.any { it.returnType == SecureRandom::class.type } }

    @MethodParameters()
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == SecureRandom::class.type }
    }

    @MethodParameters()
    class shutdown : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
    }

    @MethodParameters()
    class isDone : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
    }

    class future : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Future::class.type }
    }

    class executor : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ExecutorService::class.type }
    }
}
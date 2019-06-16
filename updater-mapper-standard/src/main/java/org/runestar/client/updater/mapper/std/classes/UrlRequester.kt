package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import org.objectweb.asm.Type.*
import java.net.URL
import java.util.*

class UrlRequester : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.interfaces == listOf(Runnable::class.type) }
            .and { it.instanceFields.count { it.type == Queue::class.type } == 1 }

    class requests : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Queue::class.type }
    }

    class thread : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Thread::class.type }
    }

    class isClosed : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE }
    }

    @MethodParameters("url")
    class request : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.startsWith(URL::class.type) }
    }

    @MethodParameters()
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.name != Runnable::run.name }
    }
}
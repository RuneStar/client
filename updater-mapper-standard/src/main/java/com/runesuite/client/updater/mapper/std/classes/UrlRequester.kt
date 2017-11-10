package com.runesuite.client.updater.mapper.std.classes

import com.hunterwb.kxtra.lang.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Type.*
import java.net.URL
import java.util.*

@SinceVersion(152)
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
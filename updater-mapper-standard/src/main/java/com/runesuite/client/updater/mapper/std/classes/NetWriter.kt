package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import java.net.URL

@SinceVersion(157)
@DependsOn(NetSocket::class)
class NetWriter : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { it.type == type<NetSocket>() } }

    @DependsOn(NetSocket::class)
    class socket0 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<NetSocket>() }
    }

    @MethodParameters()
    @DependsOn(NetSocket::class)
    class socket : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<NetSocket>() }
    }

    @DependsOn(Buffer::class)
    class buffer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Buffer>() }
    }
}
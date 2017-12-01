package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Method2

@SinceVersion(160)
@DependsOn(NetSocket::class)
class AbstractSocket : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { klass<NetSocket>().superType == it.type }

    @DependsOn(NetSocket.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.close>().mark }
    }

    @DependsOn(NetSocket.readByte::class)
    class readByte : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.readByte>().mark }
    }

    @DependsOn(NetSocket.available::class)
    class available : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.available>().mark }
    }

//    @DependsOn(NetSocket.write::class)
//    class write : IdentityMapper.InstanceMethod() {
//        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.write>().mark }
//    }

    @DependsOn(NetSocket.read::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.read>().mark }
    }
}
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
import java.net.Socket

@SinceVersion(160)
@DependsOn(AbstractChannel::class)
class BufferedNetSocket : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { it.superType == type<AbstractChannel>() }

    class socket : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Socket::class.type }
    }

    @DependsOn(BufferedSink::class)
    class sink : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<BufferedSink>() }
    }

    @DependsOn(BufferedSource::class)
    class source : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<BufferedSource>() }
    }

    @MethodParameters()
    @DependsOn(NetSocket.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.close>().mark }
    }

    @MethodParameters()
    @DependsOn(NetSocket.readUnsignedByte::class)
    class readUnsignedByte : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.readUnsignedByte>().mark }
    }

    @MethodParameters()
    @DependsOn(NetSocket.available::class)
    class available : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.available>().mark }
    }

    @DependsOn(NetSocket.write::class)
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.write>().mark }
    }

    @DependsOn(NetSocket.read::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.read>().mark }
    }

    @DependsOn(NetSocket.canRead::class)
    class canRead : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.canRead>().mark }
    }
}
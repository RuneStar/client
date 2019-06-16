package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import java.net.Socket

@DependsOn(AbstractSocket::class)
class BufferedNetSocket : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { it.superType == type<AbstractSocket>() }

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

    @MethodParameters("src", "srcIndex", "length")
    @DependsOn(NetSocket.write::class)
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.write>().mark }
    }

    @MethodParameters("dst", "dstIndex", "length")
    @DependsOn(NetSocket.read::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.read>().mark }
    }

    @MethodParameters("length")
    @DependsOn(NetSocket.isAvailable::class)
    class isAvailable : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<NetSocket.isAvailable>().mark }
    }
}
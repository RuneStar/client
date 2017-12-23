package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2

@SinceVersion(160)
@DependsOn(NetSocket::class)
class AbstractChannel : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { klass<NetSocket>().superType == it.type }

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
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
import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Type.VOID_TYPE

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
    class getSocket : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<NetSocket>() }
    }

    @MethodParameters("socket")
    @DependsOn(NetSocket::class)
    class setSocket : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<NetSocket>()) }
    }

    @DependsOn(Buffer::class)
    class buffer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Buffer>() }
    }

    @DependsOn(PacketBuffer::class)
    class packetBuffer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<PacketBuffer>() }
    }

    @DependsOn(IterableNodeDeque::class)
    class packetBufferNodes : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeDeque>() }
    }

    @MethodParameters()
    @DependsOn(NetSocket.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<NetSocket.close>().id } }
    }
}
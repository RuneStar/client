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

@SinceVersion(160)
@DependsOn(AbstractChannel::class)
class PacketWriter : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { it.type == type<AbstractChannel>() } }

    @DependsOn(AbstractChannel::class)
    class channel0 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<AbstractChannel>() }
    }

    @MethodParameters()
    @DependsOn(AbstractChannel::class)
    class getChannel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<AbstractChannel>() }
    }

    @MethodParameters("channel")
    @DependsOn(AbstractChannel::class)
    class setChannel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<AbstractChannel>()) }
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
    @DependsOn(AbstractChannel.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<AbstractChannel.close>().id } }
    }
}
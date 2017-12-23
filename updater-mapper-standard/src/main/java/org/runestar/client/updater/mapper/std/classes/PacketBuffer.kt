package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Type

@DependsOn(Buffer::class)
class PacketBuffer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Buffer>() }

    class bitIndex : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Type.INT_TYPE }
    }

    class isaacCipher0 : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type in it.jar }
    }

    @MethodParameters("array")
    class newIsaacCipher : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type) }
    }

    @SinceVersion(157)
    @MethodParameters("isaacCipher")
    @DependsOn(IsaacCipher::class)
    class setIsaacCipher : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.VOID_TYPE }
                .and { it.arguments.startsWith(type<IsaacCipher>()) }
    }
}
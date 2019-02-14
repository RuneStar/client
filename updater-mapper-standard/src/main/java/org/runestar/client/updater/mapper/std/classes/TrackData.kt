package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2

class TrackData : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.size == 8 }
            .and { it.instanceFields.count { it.type == IntArray::class.type } == 4 }

    @DependsOn(Buffer::class)
    class buffer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Buffer>() }
    }

    @MethodParameters()
    class reset : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == Type.VOID_TYPE }
                .and { it.instructions.count { it.opcode == Opcodes.ACONST_NULL } == 5 }
    }
}
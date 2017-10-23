package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.INT_TYPE

class ClientPreferences : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == LinkedHashMap::class.type } == 1 }

    class windowMode : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    class parameters : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == LinkedHashMap::class.type }
    }

    @MethodParameters
    @DependsOn(Buffer::class)
    class toBuffer : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Buffer>() }
    }

    @SinceVersion(154)
    @DependsOn(toBuffer::class)
    class roofsHidden : OrderMapper.InMethod.Field(toBuffer::class, 0, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == BOOLEAN_TYPE && it.fieldOwner == type<ClientPreferences>() }
    }

    @SinceVersion(154)
    @DependsOn(toBuffer::class)
    class titleMusicDisabled : OrderMapper.InMethod.Field(toBuffer::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == BOOLEAN_TYPE && it.fieldOwner == type<ClientPreferences>() }
    }

    @SinceVersion(154)
    @DependsOn(toBuffer::class)
    class hideUsername : OrderMapper.InMethod.Field(toBuffer::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == BOOLEAN_TYPE && it.fieldOwner == type<ClientPreferences>() }
    }

    @SinceVersion(154)
    class rememberedUsername : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }
}
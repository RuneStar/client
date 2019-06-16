package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import org.objectweb.asm.Opcodes.GETFIELD
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

    @MethodParameters()
    @DependsOn(Packet::class)
    class toBuffer : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Packet>() }
    }

    @DependsOn(toBuffer::class)
    class roofsHidden : OrderMapper.InMethod.Field(toBuffer::class, 0, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == BOOLEAN_TYPE && it.fieldOwner == type<ClientPreferences>() }
    }

    @DependsOn(toBuffer::class)
    class titleMusicDisabled : OrderMapper.InMethod.Field(toBuffer::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == BOOLEAN_TYPE && it.fieldOwner == type<ClientPreferences>() }
    }

    @DependsOn(toBuffer::class)
    class hideUsername : OrderMapper.InMethod.Field(toBuffer::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == BOOLEAN_TYPE && it.fieldOwner == type<ClientPreferences>() }
    }

    class rememberedUsername : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }
}
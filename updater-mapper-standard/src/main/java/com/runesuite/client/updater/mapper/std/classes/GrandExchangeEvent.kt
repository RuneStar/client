package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@DependsOn(GrandExchangeOffer::class)
class GrandExchangeEvent : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { it.type == type<GrandExchangeOffer>() } }

    @DependsOn(GrandExchangeOffer::class)
    class grandExchangeOffer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<GrandExchangeOffer>() }
    }

    class world : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    class string1 : OrderMapper.InConstructor.Field(GrandExchangeEvent::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    class string2 : OrderMapper.InConstructor.Field(GrandExchangeEvent::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }
}
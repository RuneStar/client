package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
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
package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions

class VorbisCodebook : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 2 }
            .and { it.instanceFields.count { it.type == IntArray::class.type } == 3 }

    class dimensions : OrderMapper.InConstructor.Field(VorbisCodebook::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class multiplicands : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == FLOAT_TYPE.withDimensions(2) }
    }

    class entries : OrderMapper.InConstructor.Field(VorbisCodebook::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class lengths : OrderMapper.InConstructor.Field(VorbisCodebook::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE.withDimensions(1) }
    }

    class mults : OrderMapper.InConstructor.Field(VorbisCodebook::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE.withDimensions(1) }
    }
}
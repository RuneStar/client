package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions

class VorbisMapping :IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 2 }
            .and { it.instanceFields.count { it.type == IntArray::class.type } == 2 }

    class submaps : OrderMapper.InConstructor.Field(VorbisMapping::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class mappingMux : OrderMapper.InConstructor.Field(VorbisMapping::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class submapFloor : OrderMapper.InConstructor.Field(VorbisMapping::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE.withDimensions(1) }
    }

    class submapResidue : OrderMapper.InConstructor.Field(VorbisMapping::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE.withDimensions(1) }
    }
}
package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

@DependsOn(BufferedFile::class)
class CacheIndex : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<BufferedFile>() } == 2 }

    @DependsOn(BufferedFile::class)
    class dataFile : OrderMapper.InConstructor.Field(CacheIndex::class, -2, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == type<BufferedFile>() }
    }

    @DependsOn(BufferedFile::class)
    class indexFile : OrderMapper.InConstructor.Field(CacheIndex::class, -1, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == type<BufferedFile>() }
    }

    class index : OrderMapper.InConstructor.Field(CacheIndex::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }

    class int1 : OrderMapper.InConstructor.Field(CacheIndex::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }
}
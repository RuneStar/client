package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.UniqueMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.next
import com.runesuite.mapper.nextWithin
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes

@SinceVersion(141)
@DependsOn(Type0::class)
class LongToken : UniqueMapper.InClassInitializer.Class(Type0::class) {
    override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.LDC && it.ldcCst == Long::class.javaObjectType.type }
            .next { it.opcode == Opcodes.NEW }
}
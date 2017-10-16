package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.UniqueMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.next
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes

@SinceVersion(141)
@DependsOn(BaseVarType::class)
class StringToken : UniqueMapper.InClassInitializer.Class(BaseVarType::class) {
    override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.LDC && it.ldcCst == String::class.type }
            .next { it.opcode == Opcodes.NEW }
}
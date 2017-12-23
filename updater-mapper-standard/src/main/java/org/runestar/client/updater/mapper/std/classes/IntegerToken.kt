package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.next
import org.runestar.client.updater.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes

@SinceVersion(141)
@DependsOn(BaseVarType::class)
class IntegerToken : UniqueMapper.InClassInitializer.Class(BaseVarType::class) {
    override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.LDC && it.ldcCst == Integer::class.type }
            .next { it.opcode == Opcodes.NEW }
}
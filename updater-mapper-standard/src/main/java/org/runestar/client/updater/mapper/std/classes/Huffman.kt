package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.AllUniqueMapper
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.prevWithin
import org.runestar.client.updater.mapper.Instruction2
import org.objectweb.asm.Opcodes

class Huffman : AllUniqueMapper.Class() {
    override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.LDC && it.ldcCst == "huffman" }
            .prevWithin(4) { it.opcode == Opcodes.NEW }
}
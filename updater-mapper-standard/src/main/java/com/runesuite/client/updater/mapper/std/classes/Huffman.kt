package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.AllUniqueMapper
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.prevWithin
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes

class Huffman : AllUniqueMapper.Class() {
    override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.LDC && it.ldcCst == "huffman" }
            .prevWithin(4) { it.opcode == Opcodes.NEW }
}
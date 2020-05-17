package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.BIPUSH
import org.objectweb.asm.Opcodes.SIPUSH
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2

class Varps : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.isEmpty() }
            .and { it.instanceMethods.isEmpty() }
            .and { it.staticFields.count { it.type == IntArray::class.type } >= 3 }
            .and { it.classInitializer?.instructions?.any { it.opcode == BIPUSH && it.intOperand == 32 } ?: false }
            .and { it.classInitializer?.instructions?.any { it.opcode == SIPUSH && it.intOperand >= 2000 } ?: false }
}
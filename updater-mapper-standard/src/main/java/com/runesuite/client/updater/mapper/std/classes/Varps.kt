package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import org.objectweb.asm.Opcodes.BIPUSH
import org.objectweb.asm.Opcodes.SIPUSH

class Varps : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.isEmpty() }
            .and { it.instanceMethods.isEmpty() }
            .and { it.staticFields.count { it.type == IntArray::class.type } >= 3 }
            .and { it.classInitializer?.instructions?.any { it.opcode == BIPUSH && it.intOperand == 32 } ?: false }
            .and { it.classInitializer?.instructions?.any { it.opcode == SIPUSH && it.intOperand == 2000 } ?: false }
}
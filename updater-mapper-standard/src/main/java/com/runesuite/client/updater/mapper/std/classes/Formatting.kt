package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import org.objectweb.asm.Opcodes

class Formatting : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.isEmpty() && it.instanceMethods.isEmpty() }
            .and { it.classInitializer != null && it.classInitializer!!.instructions.any { it.opcode == Opcodes.LDC && it.ldcCst == "->" } }
}
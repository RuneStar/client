package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.objectweb.asm.Opcodes

class Formatting : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.isEmpty() && it.instanceMethods.isEmpty() }
            .and { it.classInitializer != null && it.classInitializer!!.instructions.any { it.opcode == Opcodes.LDC && it.ldcCst == "->" } }
}
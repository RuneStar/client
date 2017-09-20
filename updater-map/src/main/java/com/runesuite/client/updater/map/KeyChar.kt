package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import org.objectweb.asm.Opcodes

@SinceVersion(141)
@DependsOn(Enumerated::class, Type0::class)
class KeyChar : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(type<Enumerated>()) }
            .and { it.classInitializer != null }
            .and { it.classInitializer!!.instructions.any { it.opcode == Opcodes.GETSTATIC && it.fieldType == type<Type0>() } }
}
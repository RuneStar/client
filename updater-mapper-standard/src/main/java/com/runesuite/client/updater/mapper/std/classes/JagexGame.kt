package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.LDC
import org.objectweb.asm.Type

class JagexGame : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.interfaces.isNotEmpty() }
            .and { it.classInitializer != null }
            .and { it.classInitializer!!.instructions.any { it.opcode == LDC && it.ldcCst == "runescape" } }
            .and { it.classInitializer!!.instructions.any { it.opcode == LDC && it.ldcCst == "RuneScape" } }

    @DependsOn(Enumerated.ordinal::class)
    class ordinal : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Enumerated.ordinal>().mark }
    }

    class id : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Type.INT_TYPE }
    }

    class name : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }
}
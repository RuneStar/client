package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.UniqueMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

@SinceVersion(141)
@DependsOn(Enumerated::class)
class Type0 : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(type<Enumerated>()) }
            .and { it.classInitializer != null }
            .and { it.classInitializer!!.instructions.any { it.opcode == Opcodes.LDC && it.ldcCst == String::class.type } }

    @DependsOn(Enumerated.ordinal::class)
    class ordinal : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Enumerated.ordinal>().mark }
    }

    @DependsOn(ordinal::class)
    class id : UniqueMapper.InMethod.Field(ordinal::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.GETFIELD && it.fieldType == Type.INT_TYPE }
    }

    @DependsOn(id::class)
    class id2 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it != field<id>() }
                .and { it.type == Type.INT_TYPE }
    }
}
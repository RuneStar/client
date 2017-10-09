package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.UniqueMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@SinceVersion(141)
@DependsOn(Enumerated::class, Type0::class)
class KeyChar1 : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(type<Enumerated>()) }
            .and { it.classInitializer != null }
            .and { it.classInitializer!!.instructions.any { it.opcode == GETSTATIC && it.fieldType == type<Type0>() } }

    @DependsOn(Enumerated.ordinal::class)
    class ordinal : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Enumerated.ordinal>().mark }
    }

    @DependsOn(ordinal::class)
    class id : UniqueMapper.InMethod.Field(ordinal::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }
}
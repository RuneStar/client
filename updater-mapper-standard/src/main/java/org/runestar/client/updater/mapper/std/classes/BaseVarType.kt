package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

@SinceVersion(141)
@DependsOn(Enumerated::class)
class BaseVarType : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(type<Enumerated>()) }
            .and { it.classInitializer != null }
            .and { it.classInitializer!!.instructions.any { it.opcode == Opcodes.LDC && it.ldcCst == String::class.type } }

    @MethodParameters()
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
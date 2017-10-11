package com.runesuite.client.updater.map.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.StaticUniqueMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.nextWithin
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes

class Ignored : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.count { it.type == String::class.type } == 2 }
            .and { it.instanceFields.size == 2 }
            .and { it.constructors.all { it.arguments.isEmpty() } }

    class name : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.SIPUSH && it.intOperand == 3622 }
                .nextWithin(50) { it.opcode == Opcodes.GETFIELD && it.fieldOwner == type<Ignored>() && it.fieldType == String::class.type }
    }

    @DependsOn(name::class)
    class previousName : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type && it != field<name>() }
    }
}